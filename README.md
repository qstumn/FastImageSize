# FastImageSize
FastImageSize是一个快速获得网络图片尺寸信息和图片类型的android libary

FastImageSize通过读取图片文件头解析图片信息不需要完全下载图片，内部建有三级缓存，可以快速轻松的实现设置占位图尺寸、图片流式布局、动态调整View尺寸等功能。
    
## how to use:
### 1.gradle
```groovy
     compile 'q.rorbin:FastImageSize:1.0.3'
```
   VERSION_CODE : [here](https://github.com/qstumn/BadgeView/releases)
### 2.code
```java
int[] imageSize;
//sync
imageSize = FastImageSize.with(url).get();

//async
FastImageSize.with(url).get(new FastImageSize.ImageSizeCallback() { 

       @Override   
       public void onSizeReady(int[] size) { 
            imageSize = size;
       }
});

int imageWidth = imageSize[0];
int imageHeight = imageSize[1];
int imageType = imageSize[2];
```

或者你需要FastImageSize根据图片尺寸帮你设置View宽高 ：

```java
FastImageSize.with(url).into(imageView);
FastImageSize.with(url).override(500).into(imageView);
```
override方法可以根据你提供的限制值对宽高结果进行等比例缩放


FastImageSize默认使用UrlConnection获取InputStream读取图片的文件头，可以替换成你自己使用的第三方库，按以下方法调用即可

```java
FastImageSize.with(url).customProvider(yourProvider).get();
```

FastImageSize默认使用LruCache和DiskLruCache实现内存缓存和磁盘缓存，如果你不需要缓存每次都要重新读取，按以下方法调用即可
```java
FastImageSize.with(url).setUseCache(false).into(imageView);
```

## Thanks For

* http://blog.csdn.net/ryfdizuo/article/details/41250775
* http://blog.csdn.net/adzcsx2/article/details/50503506
* https://github.com/sdsykes/fastimage

# LICENSE
```
Copyright 2016, RorbinQiu

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
