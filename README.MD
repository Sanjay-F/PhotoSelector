# PhotoSelector

Photo Selector for Android device. Support single choice and multi-choice.



###ART
![Example1](art/demo_1.png) ![Select1](art/demo2.png)


-------------------


###Quick Start 


* Step 1
  

			ImgOptions options = new ImgOptions(selectedMode, showCamera);
            startActivityForResult(SelectedPhotoActivity.makeIntent(MainActivity.this, options), REQUEST_IMAGE);
	
 

* Step 2
Receive result 

 
		@Override
	    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	        super.onActivityResult(requestCode, resultCode, data);
	        if (requestCode == REQUEST_IMAGE) {
	            if (resultCode == RESULT_OK) {
	                ArrayList<ImageBean> imgList = data.getParcelableArrayListExtra(EXTRA_DATA);
	
	                for (ImageBean imageBean : imgList) {
	                    mResultText.append(imageBean.path);
	                    mResultText.append("\n");
	                }	            
	            }
	        }
	    }

  

 
 
---

### Thanks

这个项目是基于[MultiImageSelector](https://github.com/lovetuzitong/MultiImageSelector)的改进  