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
	        implementation 'com.github.hashonetech:crop-module:v1.2.6'
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
            val cropIntent = Crop.build(
                originalImageFilePath = originalImageFilePath,
                cropDataSaved = myCropDataSaved,
                cropState = null,
                croppedImageBitmap = null,
                useDefaults = true/false,
                forceCrop = true/false
            ) {

                //TODO: Crop Ratios
                mAspectRatio = arrayListOf()

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
                    title = "",
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
                    titleSize = 12F,
                    originalTitle = "Original",
                    squareTitle = "Square",
                    circleTitle = "Circle",
                )


                //TODO: Bottom Icon & Text
                bottomPanelBuilder = Crop.BottomPanelBuilder(
                    cropBottomBackgroundColor = com.hashone.cropper.R.color.white,
                    dividerColor = com.hashone.cropper.R.color.white,
                    doneButtonBuilder = Crop.ButtonBuilder(
                        textColor = com.hashone.cropper.R.color.black,
                        icon = com.hashone.cropper.R.drawable.ic_check_croppy_selected,
                        buttonText = "",
                        textFont = com.hashone.cropper.R.font.roboto_medium,
                        textSize = 16F,
                    ),
                    cancelButtonBuilder = Crop.ButtonBuilder(
                        textColor = com.hashone.cropper.R.color.black,
                        icon = com.hashone.cropper.R.drawable.ic_cancel,
                        buttonText = "",
                        textFont = com.hashone.cropper.R.font.roboto_medium,
                        textSize = 16F,
                    ),
                )
            }

            mActivityLauncher.launch(
                Crop.open(activity = this, cropIntent),
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

### OLD version AspectRatios list (Ratios and Shapes)

```kotlin
val aspectRatios = listOf(
    CropAspectRatio(
        id = 1,
        title = "Original",
        aspectRatio = AspectRatio.Original,
        img = R.drawable.ic_orginal_crop
    ),
    /*CropAspectRatio(
        title = "Free",
        aspectRatio = AspectRatio.Original,
        img = R.drawable.ic_aspect_free
    ),*/
    CropAspectRatio(
        id = 2,
        title = "Square",
        aspectRatio = AspectRatio(1 / 1f),
        img = R.drawable.ic_square_crop
    ),
    CropAspectRatio(
        id = 3,
        isShape = true,
        outlineType = OutlineType.Oval,
        cropOutline = OvalCropShape(id = 3, title = "Oval"),
        title = "Circle",
        aspectRatio = AspectRatio(1 / 1.000001f),
        img = R.drawable.ic_circle_crop
    ),
    CropAspectRatio(
        id = 4,
        title = "2:3",
        aspectRatio = AspectRatio(2 / 3f),
        img = R.drawable.ic_2_3_crop
    ),
    CropAspectRatio(
        id = 5,
        title = "3:4",
        aspectRatio = AspectRatio(3 / 4f),
        img = R.drawable.ic_3_4_crop
    ),
    CropAspectRatio(
        id = 6,
        title = "4:5",
        aspectRatio = AspectRatio(4 / 5f),
        img = R.drawable.ic_4_5_crop
    ),
    CropAspectRatio(
        id = 7,
        title = "9:16",
        aspectRatio = AspectRatio(9 / 16f),
        img = R.drawable.ic_9_16_crop
    ),
    CropAspectRatio(
        id = 8,
        title = "3:2",
        aspectRatio = AspectRatio(3 / 2f),
        img = R.drawable.ic_3_2_crop
    ),
    CropAspectRatio(
        id = 9,
        title = "4:3",
        aspectRatio = AspectRatio(4 / 3f),
        img = R.drawable.ic_4_3_crop
    ),
    CropAspectRatio(
        id = 10,
        title = "5:4",
        aspectRatio = AspectRatio(5f / 4f),
        img = R.drawable.ic_5_4_crop
    ),
    CropAspectRatio(
        id = 11,
        title = "16:9",
        aspectRatio = AspectRatio(16 / 9f),
        img = R.drawable.ic_16_9_crop
    ),
)
```

 ### AspectRatios list (Ratios and Shapes)

   ```kotlin
	//TODO: For migration OLD to New Version - Do not use id = 3, It is allocated for OLD Crop Shape "Circle"

	aspectRatios = arrayListOf(

                //TODO: Original Crop Ratio
                OriginalRatioData(
                    id = 1,
                    title = getString(R.string.label_original),
                    img = R.drawable.ic_orginal_crop,
                    cropOutline = RectCropShape(id = 0, title = "Rect")
                ),

                //TODO: AspectRatios
                RatioData(
                    id = 2,
                    title = getString(R.string.label_square),
                    img = R.drawable.ic_square_crop,
                    ratioValue = 1F / 1F,
                    cropOutline = RectCropShape(id = 0, title = "Rect")
                ),
		//TODO: For migration to New Version - Do not use id = 3
                RatioData(
                    id = 4,
                    title = "2:3",
                    img = R.drawable.ic_2_3_crop,
                    ratioValue = 2F / 3F,
                    cropOutline = RectCropShape(id = 0, title = "Rect")
                ),
                RatioData(
                    id = 5,
                    title = "3:4",
                    img = R.drawable.ic_3_4_crop,
                    ratioValue = 3F / 4F,
                    cropOutline = RectCropShape(id = 0, title = "Rect")
                ),
                RatioData(
                    id = 6,
                    title = "4:5",
                    img = R.drawable.ic_4_5_crop,
                    ratioValue = 4F / 5F,
                    cropOutline = RectCropShape(id = 0, title = "Rect")
                ),
                RatioData(
                    id = 7,
                    title = "9:16",
                    img = R.drawable.ic_9_16_crop,
                    ratioValue = 9F / 16F,
                    cropOutline = RectCropShape(id = 0, title = "Rect")
                ),
                RatioData(
                    id = 8,
                    title = "3:2",
                    img = R.drawable.ic_3_2_crop,
                    ratioValue = 3F / 2F,
                    cropOutline = RectCropShape(id = 0, title = "Rect")
                ),
                RatioData(
                    id = 9,
                    title = "4:3",
                    img = R.drawable.ic_4_3_crop,
                    ratioValue = 4F / 3F,
                    cropOutline = RectCropShape(id = 0, title = "Rect")
                ),
                RatioData(
                    id = 10,
                    title = "5:4",
                    img = R.drawable.ic_5_4_crop,
                    ratioValue = 5F / 4F,
                    cropOutline = RectCropShape(id = 0, title = "Rect")
                ),
                RatioData(
                    id = 11,
                    title = "16:9",
                    img = R.drawable.ic_16_9_crop,
                    ratioValue = 16F / 9F,
                    cropOutline = RectCropShape(id = 0, title = "Rect")
                ),

                //TODO: Shapes
                ShapeData(
                    id = 12,
                    title = getString(R.string.label_radius),
                    img = R.drawable.ic_square_crop,
                    shapeImg = R.drawable.ic_radius_shape
                ),
                ShapeData(
                    id = 13,
                    title = getString(R.string.label_circle),
                    img = R.drawable.ic_circle_crop,
                    shapeImg = R.drawable.ic_circle_shape
                ),
                ShapeData(
                    id = 14,
                    title = getString(R.string.label_triangle),
                    img = R.drawable.ic_triangle_crop,
                    shapeImg = R.drawable.ic_triangle_shape
                ),
                ShapeData(
                    id = 15,
                    title = getString(R.string.label_star),
                    img = R.drawable.ic_star_crop,
                    shapeImg = R.drawable.ic_star_shape
                ),
                ShapeData(
                    id = 16,
                    title = getString(R.string.label_heart),
                    img = R.drawable.ic_heart_crop,
                    shapeImg = R.drawable.ic_heart_shape
                ),
                ShapeData(
                    id = 17,
                    title = getString(R.string.label_insquare),
                    img = R.drawable.ic_insquare_crop,
                    shapeImg = R.drawable.ic_insquare_shape
                ),
                ShapeData(
                    id = 18,
                    title = getString(R.string.label_inarc),
                    img = R.drawable.ic_inarc_crop,
                    shapeImg = R.drawable.ic_inarc_shape
                ),
                ShapeData(
                    id = 19,
                    title = getString(R.string.label_heptagon),
                    img = R.drawable.ic_heptagon_crop,
                    shapeImg = R.drawable.ic_heptagon_shape
                ),
                ShapeData(
                    id = 20,
                    title = getString(R.string.label_pentagon),
                    img = R.drawable.ic_pentagon_crop,
                    shapeImg = R.drawable.ic_pentagon_shape
                ),
                ShapeData(
                    id = 21,
                    title = getString(R.string.label_hexagon),
                    img = R.drawable.ic_hexagon_crop,
                    shapeImg = R.drawable.ic_hexagon_shape
                ),
            )

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


