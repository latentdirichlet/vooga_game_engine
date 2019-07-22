# Design Guidelines for Player Environment

## view
* Listen to (observe) Engine
* Display mainGame view accordingly
* Hold all view objects and handle various types of displays
* Call methods in controller based on user input and/or mainGame interactions
* Runs the mainGame loop within an instance of Application (Game), updating the Display at each frame

## controller
* Methods in this package take user input and mainGame interaction info from view
* Updates the necessary data in the engine
