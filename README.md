# crop-module
Media Gallery module, used to select Photo, Video, Capture Photo or Video from Camera.

[![](https://jitpack.io/v/hashonetech/crop-module.svg)](https://jitpack.io/#hashonetech/crop-module)

	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
  
	dependencies {
	        implementation 'com.github.hashonetech:crop-module:Tag'
	}

In AndroidManifest.xml

     <uses-permission android:name="android.permission.READ_MEDIA_IMAGES" />
    <uses-permission android:name="android.permission.POST_NOTIFICATIONS" /> <!-- Notification Permission -->
    <uses-permission
        android:name="android.permission.WRITE_EXTERNAL_STORAGE"
        tools:ignore="ScopedStorage" />
    <uses-permission
        android:name="android.permission.READ_EXTERNAL_STORAGE"
        android:maxSdkVersion="32" />

	<application
		...
		tools:replace="android:theme,android:name">
		...

		<provider
		    android:name="androidx.core.content.FileProvider"
		    android:authorities="${applicationId}.provider"
		    ...
		    tools:replace="android:resource"
		    ...>
		</provider>
	 </application>
	 
 Implementation

  private var myCropDataSaved: CropDataSaved? = null
	
  if (checkPermissions()) {
            mActivityLauncher.launch(
                Crop.open(activity = this, Crop.build(
                    originalImageFilePath = originalImageFilePath,
                    cropDataSaved = myCropDataSaved,
                ) {
                    cropState = null
                    croppedImageBitmap = null
                    //TODO: Screen
                    windowBackgroundColor = com.hashone.cropper.R.color.extra_extra_light_gray_color
                    statusBarColor = com.hashone.cropper.R.color.extra_extra_light_gray_color
                    navigationBarColor = com.hashone.cropper.R.color.white

                    //TODO: Toolbar
                    toolBarColor = com.hashone.cropper.R.color.white
                    backPressIcon = com.hashone.cropper.R.drawable.ic_back
                    backPressIconDescription = ""
                    toolBarTitle = "Crop"
                    toolBarTitleColor = com.hashone.cropper.R.color.black
                    toolBarTitleFont = com.hashone.cropper.R.font.outfit_regular
                    toolBarTitleSize = 16F

                    //TODO: AspectRatio
                    aspectRatioBackgroundColor = com.hashone.cropper.R.color.white
                    aspectRatioSelectedColor = com.hashone.cropper.R.color.black
                    aspectRatioUnSelectedColor = com.hashone.cropper.R.color.dark_gray_color_2
                    aspectRatioTitleFont = com.hashone.cropper.R.font.roboto_medium

                    //TODO: Bottom Icon & Text
                    cropDoneTextColor = com.hashone.cropper.R.color.black
                    cropDoneIcon = com.hashone.cropper.R.drawable.ic_check_croppy_selected
                    cropDoneText = "Crop"
                    cropDoneTextFont = com.hashone.cropper.R.font.roboto_medium
                    cropDoneTextSize = 16F

                    cropCancelTextColor = com.hashone.cropper.R.color.black
                    cropCancelIcon = com.hashone.cropper.R.drawable.ic_cancel
                    cropCancelText = "Skip"
                    cropCancelTextFont = com.hashone.cropper.R.font.roboto_medium
                    cropCancelTextSize = 16F

                    cropBottomBackgroundColor = com.hashone.cropper.R.color.white
                    dividerColor = com.hashone.cropper.R.color.extra_extra_light_gray_color

                }),
                onActivityResult = object : BetterActivityResult.OnActivityResult<ActivityResult> {
                    override fun onActivityResult(activityResult: ActivityResult) {
                        if (activityResult.resultCode == Activity.RESULT_OK) {
                            activityResult.data?.let { intentData ->
                                if (intentData.hasExtra(CropActivity.KEY_RETURN_CROP_DATA)) {
                                    myCropDataSaved =
                                        intentData.extras?.serializable<CropDataSaved>(CropActivity.KEY_RETURN_CROP_DATA)
                                    Glide.with(this@MainActivity).load(myCropDataSaved!!.cropImg)
                                        .into(mBinding.cropedImage)
                                }
                            }
                        }
                    }
                }
            )
        }
