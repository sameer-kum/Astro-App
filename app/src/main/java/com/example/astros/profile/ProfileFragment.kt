package com.example.astros.profile

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.astros.R
import com.example.astros.databinding.FragmentProfileBinding
import com.example.astros.databinding.ProfilePickerDialogBinding
import com.example.astros.service.SharedPreference
import com.example.astros.updateprofile.UpdateProfileViewModel
import com.example.astros.utils.GeneralUtil
import com.example.astros.utils.NavigationUtils
import java.io.File
import java.io.FileOutputStream

class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private lateinit var sharedPreference: SharedPreference
    private lateinit var updateProfileViewModel: UpdateProfileViewModel
    private lateinit var profileViewModel: ProfileViewModel
    private val requestCameraPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
            if (isGranted) {
                openCamera()
            } else {
                GeneralUtil.showToast(requireContext(), "Camera permission denied")
            }
        }

    private val captureImageLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val bitmap = result.data?.extras?.get("data") as? Bitmap
                if (bitmap != null) {
                    val imageFile = saveImageToStorage(bitmap)
                    if (imageFile != null) {
                        sharedPreference.saveProfileImage(imageFile.absolutePath)
                        binding.ivprofile.setImageBitmap(bitmap)

                        updateProfileViewModel.updateProfilePhoto(
                            token = sharedPreference.getToken().toString(), // Get user token
                            imageFile = imageFile,
                            onSuccess = {
                                GeneralUtil.showToast(requireContext(), "Profile photo updated successfully")
                            },
                            onError = { error ->
                                GeneralUtil.showToast(requireContext(), error)
                            }
                        )
                    }
                }
            }
        }

    private val pickImageLauncher =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            if (uri != null) {
                val imageFile = convertUriToFile(uri)
                if (imageFile != null) {
                    sharedPreference.saveProfileImage(uri.toString())
                    binding.ivprofile.setImageURI(uri)

                    updateProfileViewModel.updateProfilePhoto(
                        token = sharedPreference.getToken().toString(),
                        imageFile = imageFile,
                        onSuccess = {
                            GeneralUtil.showToast(requireContext(), "Profile photo updated successfully")
                        },
                        onError = { error ->
                            GeneralUtil.showToast(requireContext(), error)
                        }
                    )
                }
            }
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        updateProfileViewModel = UpdateProfileViewModel()
        profileViewModel = ProfileViewModel()
        sharedPreference = SharedPreference(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        subscribeUI(binding)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadProfileImage()

        binding.llProfile.setOnClickListener {
            showProfilePickerDialog()
        }

        binding.btnUpdateProfile.setOnClickListener {
            NavigationUtils.navigateWithAnimation(findNavController(), R.id.action_profileFragment_to_updateProfileFragment)
        }

        binding.btnDeleteProfile.setOnClickListener {
            profileViewModel.deleteAccount(sharedPreference.getToken().toString())
        }

    }


    private fun subscribeUI(binding: FragmentProfileBinding) {
        profileViewModel.deleteAccountResult.observe(viewLifecycleOwner, Observer { result ->
            result.onSuccess { message ->
                GeneralUtil.showToast(requireContext(), message)
                NavigationUtils.navigateWithAnimation(findNavController(), R.id.action_profileFragment_to_loginFragment)
            }.onFailure { error ->
                GeneralUtil.showToast(requireContext(), error.message ?: "Error occurred")
            }
        })
    }


    private fun showProfilePickerDialog() {
        val dialogBinding =
            ProfilePickerDialogBinding.inflate(LayoutInflater.from(requireContext()))

        // Create an AlertDialog and set the custom view
        val alertDialog = AlertDialog.Builder(requireContext())
            .setView(dialogBinding.root)
            .setCancelable(true)
            .create()

        dialogBinding.llCamera.setOnClickListener {
            GeneralUtil.showToast(requireContext(), "Camera selected")
            checkCameraPermissionAndOpenCamera()
            alertDialog.dismiss()
        }

        dialogBinding.llGallery.setOnClickListener {
            GeneralUtil.showToast(requireContext(), "Gallery selected")
            openGallery()
            alertDialog.dismiss() // Dismiss the dialog after selection
        }

        dialogBinding.tvCancel.setOnClickListener {
            alertDialog.dismiss()
        }

        // Show the dialog
        alertDialog.show()
    }

    private fun checkCameraPermissionAndOpenCamera() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            openCamera()
        } else {
            requestCameraPermissionLauncher.launch(android.Manifest.permission.CAMERA)
        }
    }

    private fun openCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        captureImageLauncher.launch(intent)
    }

    private fun openGallery() {
        pickImageLauncher.launch("image/*")
    }

    private fun saveImageToStorage(bitmap: Bitmap): File? {
        return try {
            val file = File(requireContext().filesDir, "profile_image.jpg")
            val outputStream = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, outputStream)
            outputStream.flush()
            outputStream.close()
            file
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    private fun convertUriToFile(uri: Uri): File? {
        val inputStream = requireContext().contentResolver.openInputStream(uri)
        val file = File(requireContext().cacheDir, "temp_profile.jpg")
        inputStream?.use { input ->
            file.outputStream().use { output ->
                input.copyTo(output)
            }
        }
        return file
    }

    private fun loadProfileImage() {
        val profileImageUriString = sharedPreference.loadProfileImage()
        Log.d("GetSavedImg", "Retrieved image URI: $profileImageUriString")


        if (profileImageUriString != null) {
            val profileImageUri = Uri.parse(profileImageUriString)  // Convert the saved string back to a URI

            try {
                // Use ContentResolver to open an InputStream from the URI
                val inputStream = requireContext().contentResolver.openInputStream(profileImageUri)
                val bitmap = BitmapFactory.decodeStream(inputStream)  // Decode the image from the stream
                binding.ivprofile.setImageBitmap(bitmap)  // Set the image in the ImageView
                inputStream?.close()  // Close the input stream
            } catch (e: Exception) {
                e.printStackTrace()
                GeneralUtil.showToast(requireContext(), "Error loading image")
            }
        }
    }

}