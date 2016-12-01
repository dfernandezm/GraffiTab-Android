<img src="https://drive.google.com/uc?export=download&id=0B8cKnJyOSiKfQkw5c0dWUVppaUU" width="1500">

The GraffiTab app lets you create and share drawings with your audience. Follow your favourite artists, be creative and build your profile.

This is the Android app for the system. Work is still in progress.

## Most important files (growing list)

* `AndroidManifest.xml` - Main configuration for the app. Includes supported and required features.
* `GenericItemListFragment.java` - The base of all list-based fragments. Provides pull-to-refresh and endless-scrolling functionality. It cannot be used directly and must be subclassed. It also handles the network requests that fetch the item lists.
* `AdvancedRecyclerView.java` - A custom version of Android's RecyclerView which is used in combination with the GenericItemListFragment. This version of the RecyclerView provides support for headers/footers and load-more plus pull-to-refresh functionality. It includes several components - `AdvancedRecycleView`, `AdvancedRecyclerViewLayoutConfiguration`, `AdvancedRecyclerViewItemDecoration`, `AdvancedRecyclerViewAdapter` and `AdvancedEndlessRecyclerViewAdapter`.
* `GenericStreamablesFragment.java` - An example of how the GenericItemListFragment might be subclassed.
* `GenericStreamablesRecyclerViewAdapter.java` - Used alongside `GenericStreamablesFragment` to provide the main views for the different collection types.
* `UserProfileFragment.java` - Custom implementation of `GenericItemListFragment` which shows how to switch between different layout times at runtime. It also outlines how to attach a header to the `RecyclerView`.
* `HomeActivity.java` - The home screen for the app. Shows how to use ` `ToolBar` and `ViewHolder` to create tabbed layout with fragments. Also shows how to use the `RESideMenu` library.
* `FacebookUtilsActivity.java` - Custom Facebook-helper activity which handles login with Facebook and validating the user's Facebook tokens.
* `CameraUtilsActivity.java` - Activities requiring image capture should extend this class. It provides an image source picker as well as a cropper.

## License

Copyright 2016 GraffiTab

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
