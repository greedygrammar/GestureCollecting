GestureCollecting

The purpose of this program is to demonstrate how to collect time, acceleration and gyroscope from the touchscreen based on Android. The difference between GestureCollecting and DataCollecting is that G also collect gesture count, for example, on_down_count on_scroll_count and so on, and also G has recorded the location of pressing the touchscreen.
User has to register first with his username, password, sex, and age. After that, user can choose to get into the train mode or the test mode to store his biometric pattern.

Program Design

The most important class in this program is the DataCollecting class extends GestureDetector.

the GestureCollecting class initializes the SensorManager Objects with the appropriate OnResume, OnPause and OnSensorChanged method. And also listening to the location of finger press and different gesture count.

When users click the button, we record the gesture count with the user's clicking time in millisecond, acceleration, gyroscope, pressure, location, distance, velocity and size.
