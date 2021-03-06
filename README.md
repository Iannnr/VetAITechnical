# VetAI Technical

Setup Notes:

Project was created using Android Studio 4.1 RC-2, to use on the stable version:

`classpath "com.android.tools.build:gradle:4.1.0-rc02"` 

may need to be changed to

`classpath "com.android.tools.build:gradle:4.0.0"` 

The Sandbox keys have been omitted from the app [build.gradle](https://github.com/Iannnr/VetAITechnical/blob/master/app/build.gradle#L34) for security reasons.
To ensure the app builds successfully, the following code needs to be inserted on [line 34](https://github.com/Iannnr/VetAITechnical/blob/master/app/build.gradle#L34) : (with the keys inserted from the webpage provided)
```groovy
debug {
            buildConfigField "String", BASE_URL, "\"https://sandbox-api.brewerydb.com/v2/\""
            buildConfigField "String", SANDBOX_KEY, "\"...\""
            buildConfigField "String", SANDBOX_KEY_2, "\"...\""
        }
```

# Some known issues & unfinished tasks

* Pressing the back button (hardware + UI element) will pop all fragments before closing, leaving a white screen and requiring one more back press before minimizing rather than closing the app while on the beer list fragment
* no external exception logging
* no unit tests, but methods in the repo are well enough split up to do so
* placeholder images for a beer image - use of Coil/Glide/Picasso for implementation
* checkbox instead of the favourite heart - easiest way to achieve a checked change listener + logic based on interaction
* no HTTP response code checking/validation
* no paging for the API response, extension functions for RxJava + Kotlin makes this quite easy, but first 50 results was enough to show concept
* no cache control nor rate limiting checking
* no proguard nor keystore setup for a signed release
* better fragment management needed, it currently only replaces, this whole approach could just be handled by a Navigation graph
* no real use of styles or colours
* snackbar and the use of lambda callbacks is less than ideal here

# Justification of choices

* I chose RxJava for its ability to chain flows together and switching/mapping functions, I understand this can also be done with Coroutines with a list of deferred jobs and `awaitAll()` and suspend functions too. Along with built-in support for suspend functions with both Room and Retrofit.
* Disposables is also a good way to cancel an execution of a job, similar to a scope of a coroutine job, which can be cleared on the VM / View lifecycle end
* I'd prefer to use the Android Paging Library for the API call & RecyclerView Adapter, but the set up time is significantly longer than just a RecyclerView, Adapter & an async diff config
* This app implements a mixture of Databinding, I feel like this is absolutely fine to use for the ViewHolder & the chosen beer information view, as it saves on a reasonable amount of boilerplate code, but understand that it's not the fastest and debugging it can be difficult
* I chose Hilt over Dagger here because of its smaller start up overhead and reduction of code required for injections (especially for ViewModels) and graph creation. I understand this library isn't entirely production ready, but offered enough of the basics of requiring providers and modules for Dagger, the main thing "missing" from it is the Components, as these are already provided by Hilt Interfaces.
* The individual beer view doesn't use injected objects from the dagger graph, as it needs a run-time object, this can be injected using [Assisted Inject](https://github.com/square/AssistedInject) , but this cannot be used in conjunction with `@ViewModelInject` or other provided objects, so it gets messy, with specific objects requiring to provide them...
* the website [JSON to Kotlin Data Class](https://www.json2kotlin.com/) was used to create the classes for the retrofit serialization, as a time-saving aspect

#
I'd say I did spend a bit more than 8 hours on this, especially with comments, markdown, setup requirements, double checking it works with other Android Studio versions, screenshots etc, but it was a fun task and could have quick happily spent more time on it.

# Screenshots 

![Horizontal 1 Favourite](https://github.com/Iannnr/VetAITechnical/blob/master/screenshots/Screenshot_1601141369.png "")


![Vertical 1 Favourite](https://github.com/Iannnr/VetAITechnical/blob/master/screenshots/Screenshot_1601141382.png "")


![Vertical 2 Favourites](https://github.com/Iannnr/VetAITechnical/blob/master/screenshots/Screenshot_1601141393.png "")


![Vertical Filtered Favourites](https://github.com/Iannnr/VetAITechnical/blob/master/screenshots/Screenshot_1601141396.png "")


![Beer View 1](https://github.com/Iannnr/VetAITechnical/blob/master/screenshots/Screenshot_1601141401.png "")


![Beer View 2](https://github.com/Iannnr/VetAITechnical/blob/master/screenshots/Screenshot_1601141412.png "")


![Beer View Scrolled](https://github.com/Iannnr/VetAITechnical/blob/master/screenshots/Screenshot_1601141420.png "")

