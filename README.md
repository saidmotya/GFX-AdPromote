[![](https://jitpack.io/v/saidmotya/GFX-AdPromote.svg)](https://jitpack.io/#saidmotya/GFX-AdPromote)

# GFX-AdPromote-Apps

An Android library for member secretGFX group, This can be used to growing your apps and get more install via a simple banner view & native view and interstitial dialog.

# GFX-AdPromote-Youtube
Added YoutubePromote to promote your channel inside your application android and get more traffic to your videos (Views & Subscribe ) with attractif design :
*1- Intersitital :* 2 style.
*2- Banner       :* 320*50 dp.
*3- Native       :* 3 type of native > Full Native, Banner Native, Native without preview player.

## Youtube Preview :

*Native Example*
![Native Example](https://github.com/saidmotya/GFX-AdPromote/blob/master/ScreenShot/NativePreview.png)

*Interstitial Example*
![Interstitial Example](https://github.com/saidmotya/GFX-AdPromote/blob/master/ScreenShot/InterstitialPreview.png)


## Installation
*Step 1.* Add the JitPack repository to your project `build.gradle` file
```gradle
allprojects {
    repositories {
        //your repository
        maven { url 'https://jitpack.io' }
    }
}
```
*Step 2.* Add the dependency in the form
```gradle
dependencies {
    implementation 'com.github.saidmotya:GFX-AdPromote:1.0.4'
}
```

## Initialize
In your Java code, you can initialize the library with two way : inside splash with param Activity and check if the lib is connected than start the normal activity Or in MyApplication extend Application with param Context, see the full code source for more details :
```java
AppPromote.initializePromote(this,"Your link json her");
        AppPromote.setOnPromoteListener(new OnPromoteListener() {
            @Override
            public void onInitializeSuccessful() {
                //AppPromote onInitializeSuccessful
            }

            @Override
            public void onInitializeFailed(String error) {
                //AppPromote onInitializeFailed
            }
        });
```


## How to use Banner Promote :
Place your banner promote in your xml layout like this:
```xml
 <com.gfx.adPromote.BannerPromote
        android:id="@+id/banner_view"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        app:banner_bodyColor="#FFFFFF"
        app:banner_contentColor="#E41000"
        app:banner_installColor="#FF5722"
        app:banner_installTitle="Install"
        />
```

#### Banner Promote Custom Attributes
| Attribute | Description |
| --- | --- |
| `banner_bodyColor` | Color of the banner body (by default is white) |
| `banner_contentColor` | Color of the banner content : title and description (description by default color : gray) |
| `banner_installColor` | Install button color : default is blue |
| `banner_installTitle` | Title of button : default is "Install" |


## Banner Promote in Java code :
In your Java code, you can initialize your banner view and get listener, you can make the attributes of banner progrmatically or in xml layout :
```java
 BannerPromote bannerPromote = findViewById(R.id.banner_view);
        //bannerPromote.setInstallTitle("Play"); //Banner title button
        //bannerPromote.setInstallColor("#FFC107"); //Title button color with param String or Resource color
        //bannerPromote.setDescriptionColor(R.color.my_color_description);//Description Text color with param String or Resource color
        bannerPromote.setOnBannerListener(new OnBannerListener() {
            @Override
            public void onBannerAdLoaded() {
                //banner loaded.
            }

            @Override
            public void onBannerAdClicked() {
                //banner clicked.
            }

            @Override
            public void onBannerAdFailedToLoad(String error) {
                //banner failed to load.

            }
        });
```
## Banner Promote Example
![Banner Promote Example](https://github.com/saidmotya/GFX-AdPromote/blob/master/ScreenShot/banner_promo.gif)



## How to use Native promote :
Place your native promote in your xml layout like this:
```xml
 <com.gfx.adPromote.NativePromote
        android:id="@+id/native_ad"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        app:native_installColor="#4CAF50"
        app:native_installRadius="5"
        app:native_installTitle="Install" />
```

#### Native Promote Custom Attributes
| Attribute | Description |
| --- | --- |
| `native_installTitle` | Title button of native, default : "Install" |
| `native_installColor` | Color of the button native (by default color : blue) |
| `native_contentColor` | Content color (Title of the app and description) |
| `native_bodyColor` | Native Body background color : white is the default |
| `native_installRadius` | Raduis of button install : corner of button, defualt is 20px |


## Native Promote in Java code :
In your Java code, you can initialize your native view and get listener :
```java
 NativePromote nativePromote = findViewById(R.id.native_ad);
        nativePromote.setButtonColor("#1C7DCA");
        nativePromote.setButtonTitle("Play Now");
        nativePromote.setRadiusButton(10);  //corner of button radius.
        nativePromote.setOnNativeListener(new OnNativeListener() {
            @Override
            public void onNativeAdLoaded() {
                //Native loaded.
            }

            @Override
            public void onNativeAdClicked() {
                //Native clicked.
            }

            @Override
            public void onNativeAdFailedToLoad(String error) {
                //"Native failed to load
            }
        });
```
## Native Promote Example
![Native Promote Example](https://raw.githubusercontent.com/saidmotya/GFX-AdPromote/master/ScreenShot/nativePromo.png)


## Interstitial Promote in Java code :
Initialize the interstitial promote inside your code and get more controller the attributes programmatically :
```java
  InterstitialPromote interstitialPromote = new InterstitialPromote(MainActivity.this);
        interstitialPromote.setStyle(InterstitialStyle.Advance);
        interstitialPromote.setInstallColor(R.color.my_color); //color of button from resource.
        //interstitialAd.setInstallColor("#E91E63"); //color of button from string.
        interstitialPromote.setTimer(5);//5 second to closed the Ad.
        interstitialPromote.setInstallTitle("Play Now");
        interstitialPromote.setRadiusButton(10); //corner of button radius.
        interstitialPromote.setOnInterstitialAdListener(new OnInterstitialAdListener() {
            @Override
            public void onInterstitialAdLoaded() {
                //interstitialAd loaded.
            }

            @Override
            public void onInterstitialAdClosed() {
                //interstitialAd closed.
            }

            @Override
            public void onInterstitialAdClicked() {
                //interstitialAd clicked.
            }

            @Override
            public void onInterstitialAdFailedToLoad(String error) {
                //Interstitial failed to load
            }
        });

```
Attributes programmatically for the `InterstitialPromote`:
* `setStyle` Style of interstitial containt 2 style : InterstitialStyle.Advance or InterstitialStyle.Standard.
* `setInstallColor` Color of install ad button : blue is default color.
* `setTimer` CountDown time in Close button, you can make any time to block the close button : default is 0 (hide countDown).
* `setInstallTitle` Title of install button : default is "Install".
* `setRadiusButton` Raduis of button install : corner of button, defualt is 20px.

## Showing Interstitial Promote :
When our lib initialize successful and check the interstitial is loaded than show him, if not start normal activity, check the code below :
```java
 //check if the interstitial is loaded yet.
                if (interstitialPromote.isAdLoaded()) {

                    interstitialPromote.show(); //call showing interstitial
                    //when user close the interstitial ad, start the next activity.
                    interstitialPromote.setOnAdClosed(new OnAdClosed() {
                        @Override
                        public void onAdClosed() {
                            //your intent
                            //start your Activity
                        }
                    });
                } else {
                    //your intent : when ad is not ready yet.
                    //start your Activity
                }
```
## Interstitial Promote Example
![Interstitial Promote Example](https://raw.githubusercontent.com/saidmotya/GFX-AdPromote/master/ScreenShot/interstitialPromo.gif)


## File json:
* The json file must to be uploaded in your server. [File Json](https://github.com/saidmotya/GFX-AdPromote/blob/master/ScreenShot/MyPromote.json)

## Help Maintenance
If you could help me to continue maintain this repo, buying me a cup of coffee will make my life really happy and get much energy out of it.

<a href="https://www.buymeacoffee.com/saidmotya" target="_blank"><img src="https://www.buymeacoffee.com/assets/img/custom_images/purple_img.png" alt="Buy Me A Coffee" style="height: auto !important;width: auto !important;" ></a>



 ## Thanks :
* The **Glide Lib** for display images. [Visit](https://github.com/bumptech/glide)
* The **Dotsindicator Lib** for indicator viewPager. [Visit](https://github.com/tommybuonomo/dotsindicator)

## License
    Copyright 2022 SAID MOTYA

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
