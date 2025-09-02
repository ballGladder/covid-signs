# COVID-19 Signs Tracker

COVID-19 Signs Tracker is an Android application designed to help users record, measure, and track vital signs and symptoms related to COVID-19. The app leverages device sensors and advanced video processing to estimate heart rate and respiratory rate, and allows users to log common COVID-19 symptoms for personal tracking or research purposes.

---

## Features

- **Heart Rate Measurement**: Record a 45-second video using your phone's camera. The app processes the video to estimate heart rate by analyzing frame redness.
- **Respiratory Rate Measurement**: Uses the phone's accelerometer to sense abdominal movement over 45 seconds and calculates breathing rate.
- **Symptom Logging**: Users can rate common COVID-19 symptoms (fever, cough, tiredness, shortness of breath, muscle aches, chills, sore throat, runny nose, headache, chest pain) using a simple interface.
- **Data Storage**: All measurements and symptom ratings are stored locally in a Room database for later review or export.
- **CSV Export**: Raw and processed sensor/video data are saved as CSV files for reference or further analysis.
- **Permissions Handling**: The app requests necessary permissions for camera, storage, and sensors at runtime.

---

## Technical Overview

- **Languages & Frameworks**: Java, Android SDK, Room (SQLite ORM), OpenCSV, JavaCV (FFmpeg, frame processing)
- **Modules**:
  - `HomeScreen`: Main activity for recording video, measuring heart/breathing rates, and uploading data.
  - `SymptomsScreen`: Activity for rating and updating symptoms.
  - `AccelerometerService`: Background service for collecting accelerometer data.
  - `HeartRateService`: Background service for extracting frames from video and calculating heart rate.
  - `AppDatabase`, `UserInfo`, `UserInfoDao`: Room database setup for storing user data.
- **Data Model**: Each record includes timestamp, heart rate, breathing rate, and ratings for 10 symptoms.

---

## How It Works

### Heart Rate Measurement

1. **Video Recording**: User records a 45s video (covering the camera lens with their finger). The app extracts frames, calculates average redness, denoises the data, and estimates heart rate using peak detection.
2. **Frame Extraction & FFmpeg**: The app uses JavaCV and FFmpeg to efficiently extract frames from the recorded video. Only 9 windows of 5 seconds each are processed, regardless of video length, for performance.
3. **Pixel-Level Optimization**: For each frame, the app calculates the average red channel value by sampling every 5th pixel in both dimensions, dramatically reducing computation while maintaining accuracy.
4. **Signal Processing**: The average redness values are denoised using a moving average filter, and heart rate is estimated by counting zero-crossings (peaks) in the processed signal.

### Respiratory Rate Measurement

1. **Sensor Activation**: User places the phone on their abdomen for 45s. The app collects accelerometer X-axis data, denoises it, and estimates respiratory rate via zero-crossing analysis.

### Symptom Logging

1. **Symptom Selection**: User selects symptoms from a dropdown and rates them using a rating bar. Data is saved and can be updated.
2. **Data Upload**: Signs and symptoms are uploaded to the local database. The latest record can be updated with new symptom ratings.

---

## Performance & Optimization

- **FFmpeg Integration**: Video frame extraction is handled by FFmpeg via JavaCV, allowing for fast, reliable access to video data on Android devices.
- **Pixel Sampling**: Instead of processing every pixel, the app samples every 5th pixel in both axes, reducing memory and CPU usage while preserving signal quality.
- **Multithreading**: Frame extraction and processing are performed in parallel threads for speed.
- **Aggressive Garbage Collection**: Explicit calls to `System.gc()` help manage memory during intensive processing.
- **Efficient Algorithms**: Moving average smoothing and zero-crossing peak detection are used for robust, real-time vital sign estimation.

---

## Setup & Installation

1. **Requirements**:
   - Android Studio (Gradle 6.6.1+, Android SDK 30+)
   - Device with camera and accelerometer

2. **Clone the Repository**:
   ```
   git clone https://github.com/yourusername/covid-signs.git
   ```

3. **Open in Android Studio**:
   - Import the project.
   - Sync Gradle and resolve dependencies.

4. **Run the App**:
   - Build and deploy to a physical device (emulator not recommended due to sensor/video requirements).

---

## Permissions

The app requires the following permissions:
- Camera
- Read/Write External Storage
- (Optional) Record Audio

These are requested at runtime.

---

## Dependencies

- [AndroidX](https://developer.android.com/jetpack/androidx)
- [Room](https://developer.android.com/jetpack/androidx/releases/room)
- [OpenCSV](http://opencsv.sourceforge.net/)
- [JavaCV/FFmpeg](https://github.com/bytedeco/javacv)

---

## Project Structure

- `app/src/main/java/com/example/covidsymptoms/` - Main source code
- `app/src/main/res/` - UI layouts and resources
- `app/schemas/` - Room database schema
- `app/build.gradle` - Module dependencies

---

## License

This project is for educational and research purposes. Please check individual dependencies for their respective licenses.

---

**Note:** This app is not a medical device and should not be used for diagnosis or treatment. For health concerns, consult a medical professional.
