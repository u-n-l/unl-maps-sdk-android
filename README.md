# unl-maps-sdk-android

# Introduction

unl-maps-sdk-android is a mapping SDK for developers of android applications, extending and enhancing functionalities of [MapLibre GL Android](https://docs.maptiler.com/maplibre-gl-native-android/). Apart from the capabilities of maplibre, the package exposes the following controls:

- Grid lines with a customisable precision;

- Tile selector, including 'vectorial', 'traffic', 'terrain', 'satellite' and 'base' options;

# Getting started

# Installation
Add the following dependency to your `build.gradle` file:

```
dependencies {
    implementation 'com.github.u-n-l:unl-maps-sdk-android:Tag'
}
```

### Add Maven URLs
```
    allprojects {
	repositories {
             ....
             maven { url 'https://jitpack.io' }
               
             maven {
                name = "GitHubPackages"
                url = uri("https://maven.pkg.github.com/u-n-l/core-java")
                credentials {
                    username = <YOUR_USERNAME>
                    password = <YOUR_PASSWORD>
                }
            }
            .....
        }
    }
```


### Enable AndroidX by adding following in `gradle.properties`.
```
android.useAndroidX=true
android.enableJetifier=true
```


## Permissons
Add the following permissions to your `AndroidManifest.xml` file:
```
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
<uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
<uses-permission android:name="android.permission.INTERNET" />
```

### Initializing the SDK

#### Example usage
#### Your `activity.xml`
```Kotlin

 <com.unl.map.sdk.views.UnlMapView
      android:id="@+id/mapView"
      android:layout_width="match_parent"
      android:layout_height="match_parent" />

```


#### Your `Activity` above `setContentView(R.layout.activity)`

```Kotlin
 override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    /// Initializing the UNL SDK
    UnlMap(applicationContext,getString(R.string.api_key),getString(R.string.vpm_id))
    setContentView(R.layout.activity)

    //Getting Instance of `UnlMapView` from `activity.xml`
    val unlMap=findViewById<UnlMapView>(R.id.mapView)

    //This `getMapAsync` method is used for map loaded to view.
    unlMap.getMapAsync {
            // Need supportFragmentManager and Activity for showing some controls
            unlMap.fm=supportFragmentManager
            unlMap.activity=this

            // enableTileSelector method is used to show TileSelector on UnlMapView or not.
            unlMap.enableTileSelector(true)
            // setGridControls method is used to show GridControls on UnlMapView or not.
            unlMap.setGridControls(this,true)
            // setGridControls method is used to set position for TileSelector.
            unlMap.setTileSelectorGravity(Gravity.RIGHT)
        }

 }
```

See [SDK Authentication Docs](https://u-n-l.github.io/unl-map-js-docs/authentication/) for instructions on how to generate an `apiKey` and `vpmId` to get started with the UnlSdk.

# Custom controls

For Custom controls, it can be accessed by assigning a class `UNLMapView`. For Example:

```Kotlin
   var mapView: UnlMapView

```

## Grid Controls

Grid controls can be enabled in your activity initialisation by passing the `setGridControls` second param as `true`. With this approach, the grid control will be initialised with the default options.

the following options can be specified during the `GridControl` initialisation:

| Option           | Type            | Default                 | Description                                                                      |
| ---------------- | --------------- | ----------------------- |--------------------------------------------------------------------------------  |
| defaultPrecision | `CellPrecision` | 9                       | Default precision of the cells. It can be changed manually via the grid selector |
| lineColor        | `ResourceColor` | default_grid_line_color | Grid line's colour                                                               |
| lineWidth        | `Float `        | 1f                      | Grid line's width                                                                |

If GridControl is enabled, the grid lines will be generated at certain zoom levels, dependant on the selected precision, according to the following table:

| Precision | Zoom level |
| --------- | ---------- |
| 10        | 20         |
| 9         | 18         |
| 8         | 16         |
| 7         | 14         |
| 6         | 12         |
| 5         | 10         |
| 4         | 8          |
| 3         | 4          |
| 2         | 3          |
| 1         | 2          |

## Tiles selector

Tile selector can be enabled in your activity, initialisation by passing the  `enableTileSelector` param `true`. With this approach, the tile selector control will be initialised with the default options.

the following options can be specified during the `TilesSelectorControl` initialisation:


| Option | Type    | Default                                                  | Description                                             |
| ------ | ------- | -------------------------------------------------------- | ------------------------------------------------------- |
| tile  | [Enum]   | TERRAIN, VECTORIAL,BASE,TRAFFIC,SATELLITE                | The options that will be included in the tiles selector |

### Custom tile selector

`UnlMapView` also provide access to use custom tile selecter with the following function:

### `Method loadStyle`

This method takes a `TileEnum` parameter and updates the selected tile from the map.

The supported TileEnum values are:  `TERRAIN`, `VECTORIAL`, `BASE`, `TRAFFIC`, `SATELLITE`.

Example

```kotlin

unlMap.loadStyle(TileEnum.BASE)

```
