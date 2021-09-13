Dependency is managed using gradle. These dependencies are required for the app:

implementation 'com.android.volley:volley:1.2.0'

implementation 'org.jsoup:jsoup:1.14.2'
	
The app can be run using a Virtual Device provided by the Android emulator in Android Studio IDE. 

Alternatively, an APK file can be created using the Android Studio IDE by going to the menubar and navigating to "Build" > "Build Bundle(s) / APK (s) > "Build APK (s)". This APK file will be saved on the PC, which can then be transferred to an android phone.



package com.example.groceryapp.activities;
contain activity classes. Each Activity class is resposible for a single screen.

package com.example.groceryapp.adapters;
contain recyclerView adapters. Each Adapter class is responsible for displaying items on the RecyclerView

package com.example.groceryapp.database;
contains DatabaseHandler class. Deals with the database.

package com.example.groceryapp.models;
contains Model classes. Used to work with data in an organised manner.

package com.example.groceryapp.scrapers;
contains Scraper classes. Responsible for web scraping

package com.example.groceryapp.touchHelpers;
contains TouchHelper classes. Responsible for implementing swipe functionality. 


Online resources used:

https://www.scrapingbee.com/java-webscraping-book/

https://www.udemy.com/course/learn-web-scraping-with-java-in-just-1-hour/

https://www.youtube.com/watch?v=4cFL7CMd5QY

https://www.youtube.com/watch?v=ZjZqLUGCWxo&t=6s

https://www.youtube.com/watch?v=312RhjfetP8

https://www.youtube.com/watch?v=7u5_NNrbQos&list=PLzEWSvaHx_Z2MeyGNQeUCEktmnJBp8136
