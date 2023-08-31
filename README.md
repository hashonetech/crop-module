# crop-module
Simple image cropping library for Android, used to crop selected image with Powerful Zoom and different ratio.

[![](https://jitpack.io/v/hashonetech/crop-module.svg)](https://jitpack.io/#hashonetech/crop-module)

```gradle
	allprojects {
		repositories {
			...
			maven { url 'https://jitpack.io' }
		}
	}
  
	dependencies {
	        implementation 'com.github.hashonetech:crop-module:v1.0.3'
	}
```

## 📸 Screenshot

<div style="display:flex;">
<img alt="App image" src="https://github.com/hashonetech/crop-module/assets/104345897/393aa5ad-a7e9-44c2-9b90-655d741e8adb" width="30%">
&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;
<img alt="App image" src="https://github.com/hashonetech/crop-module/assets/104345897/cded91f4-c456-413c-ba4b-ca287c9fb17c" width="30%">
&nbsp;&nbsp;&nbsp; &nbsp;&nbsp;&nbsp;
<img alt="App image" src="https://github.com/hashonetech/crop-module/assets/104345897/4caa9e7b-c86c-47b4-bcb3-26d764e7906b" width="30%">
</div>

## AndroidManifest.xml

```xml
    <uses-permission android:name="android.permission.READ_MEDIA_IMAGES" />
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
```
	 
 ## Implementation

   ```kotlin
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

                //TODO: Image Size 
                sizeBuilder = Crop.SizeBuilder(
                    localFileSize = 1080,
                    maxFileSize = 4096,
                )

                //TODO: AspectRatio
                aspectRatioBuilder = Crop.AspectRatioBuilder(
                    backgroundColor = com.hashone.cropper.R.color.white,
                    selectedColor = com.hashone.cropper.R.color.black,
                    unSelectedColor = com.hashone.cropper.R.color.un_select_color,
                    titleFont = com.hashone.cropper.R.font.roboto_medium,
		 	//TODO: New, title text size
                    titleSize = 12F
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
```


### License


```
Copyright 2023 Hashone Tech LLP

Licensed to the Apache Software Foundation (ASF) under one or more contributor
license agreements. See the NOTICE file distributed with this work for
additional information regarding copyright ownership. The ASF licenses this
file to you under the Apache License, Version 2.0 (the "License"); you may not
use this file except in compliance with the License. You may obtain a copy of
the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
License for the specific language governing permissions and limitations under
the License.
```


