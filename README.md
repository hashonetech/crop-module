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
## 📸 Screenshot

<div style="display:flex;">
<img alt="App image" src="https://github.com/hashonetech/crop-module/assets/104345897/393aa5ad-a7e9-44c2-9b90-655d741e8adb" width="30%">
&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;
<img alt="App image" src="https://github.com/hashonetech/crop-module/assets/104345897/cded91f4-c456-413c-ba4b-ca287c9fb17c" width="30%">
&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;
<img alt="App image" src="https://github.com/hashonetech/crop-module/assets/104345897/4caa9e7b-c86c-47b4-bcb3-26d764e7906b" width="30%">
</div>

## AndroidManifest.xml

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
	 
 ## Implementation

  
  	private var myCropDataSaved: CropDataSaved? = null
	
  	if (checkPermissions()) {
            mActivityLauncher.launch(
            Crop.build(
                originalImageFilePath = originalImageFilePath,
                cropDataSaved = myCropDataSaved,
                cropState = null,
                croppedImageBitmap = null
            ) {

                //TODO: Screen
                screenBuilder = Crop.ScreenBuilder(
                    windowBackgroundColor = com.hashone.cropper.R.color.window_bg_color,
                    statusBarColor = com.hashone.cropper.R.color.white,
                    navigationBarColor = com.hashone.cropper.R.color.white,
                    cropOuterBorderColor = com.hashone.cropper.R.color.un_select_color,
                    borderWidth = 1f,
                    borderSpacing = 2f,
                )

                //TODO: Toolbar
                toolBarBuilder = Crop.ToolBarBuilder(
                    toolBarColor = com.hashone.cropper.R.color.white,
                    backIcon = com.hashone.cropper.R.drawable.ic_back,
                    title = "Crop",
                    titleColor = com.hashone.cropper.R.color.black,
                    titleFont = com.hashone.cropper.R.font.roboto_medium,
                    titleSize = 16F,
                )


                //TODO: AspectRatio
                aspectRatioBuilder = Crop.AspectRatioBuilder(
                    backgroundColor = com.hashone.cropper.R.color.white,
                    selectedColor = com.hashone.cropper.R.color.black,
                    unSelectedColor = com.hashone.cropper.R.color.un_select_color,
                    titleFont = com.hashone.cropper.R.font.roboto_medium,
                )


                //TODO: Bottom Icon & Text
                bottomPanelBuilder = Crop.BottomPanelBuilder(
                    cropBottomBackgroundColor = com.hashone.cropper.R.color.white,
                    dividerColor = com.hashone.cropper.R.color.white,
                    doneButtonBuilder = Crop.ButtonBuilder(
                        textColor = com.hashone.cropper.R.color.black,
                        icon = com.hashone.cropper.R.drawable.ic_check_croppy_selected,
                        buttonText = "Crop",
                        textFont = com.hashone.cropper.R.font.roboto_medium,
                        textSize = 16F,
                    ),
                    cancelButtonBuilder = Crop.ButtonBuilder(
                        textColor = com.hashone.cropper.R.color.black,
                        icon = com.hashone.cropper.R.drawable.ic_cancel,
                        buttonText = "Skip",
                        textFont = com.hashone.cropper.R.font.roboto_medium,
                        textSize = 16F,
                    ),
                )
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



