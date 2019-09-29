# RxAndroidResult

RxAndroidResult is an android library that can be used for getting android activity result and permission result as a stream of RxJava [Observable](http://reactivex.io/RxJava/2.x/javadoc/io/reactivex/Observable.html)

### Goals
- Providing simple reactive activity and permission result to be handle
- Get rid of handling of activity result and permission result on the respective activity or fragment directly
and easily can be tested without setting up alot of prerequisition to the logic of each result.

### Download
Gradle : 
```Gradle
dependencies {
  implementation 'com.github.aldirenaldigunawan:rxandroidresult:[Latest Version]'
}
```

### How to use it
- Activity Result
```Kotlin
val rxActivityResult = RxActivityResultImpl(this)

fun openScreenB(){
  rxActivityResult.startActivity(intent, requestCode)
                  .filter { it.getResultStatus() == ActivityResult.ResultStatus.Ok }
                  .subscribe({
                  //handle on success
                  },{
                  //handle on error
                  })
}
```

- Permission Result
```Kotlin
val rxPermissionResult = RxPermissionImpl(this)

fun requestCameraPermission(){
    //where checkAndRequest allow vararg or multiple permission request
    rxPermissionResult.checkAndRequest(Permission.Camera)
                      .subscribe({
                      //handle permission result
                      }, {
                      //handle error
                      })
}
```


### Result 
- Activity Result
```Kotlin
/**
* requestCode is code that being request when call startActivity(intent, requestCode)
* intent data that being pass through activity result
* resultCode representative of how the intent is giving respond, either Ok, or cancel
*/
ActivityResult(requestCode, intent, resultCode)
```

- Permission Result
```Kotlin
/**
* permission result is a sealed class that has 2 child type [Granted] and [Denied]
* will carry a list of permission
*/
PermissionResult(listOfPermission)
```

#### Library Usages
- [RxJava](https://github.com/ReactiveX/RxJava)
- [RxAndroid](https://github.com/ReactiveX/RxAndroid)
