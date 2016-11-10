# FastImageSize
FastImageSize是一个不需要完全下载就可以快速获得网络图片宽高尺寸信息的android libary


####有什么用处
最适合的使用场景是在使用ListView或者RecyclerView构建的聊天界面或者朋友圈图片墙，你可能需要在itemView中加载图片时预先知道图片的大小以便给ImageView设置合适的占位宽高，不然等到图片加载完毕后，ImageView会突然变大或变小导致闪屏跳屏等情况，如果你有更好的办法和建议并愿意和我分享，请在Issues中提交给我或者给我发邮件，谢谢。
##Change Log

    v1.0.1
    修改了关流的方式,提高尺寸信息的返回速度
    
    
## how to use:
###1.gradle
```
     compile 'q.rorbin:FastImageSize:1.0.1'
```
###2.code
```
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
```
url如果是网络地址，请不要在前面省略http，FastImageSize默认采用UrlConnection获取InputStream读取图片的文件头，可以替换成OkHttp、Volley等任何你正在用的第三方框架，按以下方法调用即可

```
FastImageSize.with(url).customProvider(new InputStreamProvider() {   

     @Override   
     public InputStream getInputStream(String imagePath) { 
            // useyourmethods  
           return null;    
     }
}).get();
```
`InputStreamProvider` 实现可参考 `DefaultInputStreamProvider`

###3. 一些小问题

暂时只支持PNG、GIF、BMP、JPG格式图片，在网络不顺畅的情况下并不保证一定能获取成功，如果不使用customProvider的情况下默认超时时间为1秒，而且由于JPG图片文件头比较特殊，JPG失败概率比其他图片类型略高，建议在图片完全下载后再次取出图片宽高和之前通过FastImageSize获得的进行对比。


###4.网速流畅前提下的测试结果
ImageType | AverageTime 
---|---
jpg | 80ms 
gif | 60ms
png | 50ms
bmp | 70ms



##Thanks For

* http://blog.csdn.net/ryfdizuo/article/details/41250775
* http://blog.csdn.net/adzcsx2/article/details/50503506
* https://github.com/sdsykes/fastimage

#LICENSE
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
