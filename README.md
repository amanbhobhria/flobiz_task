# Expense Tracker App

An advanced **Expense Tracker** app built using **Kotlin**, featuring **Firebase Realtime Database**, **MVVM architecture**, **Room Database**, **Coroutines**, **LiveData**, **ViewModelFactory**, and the **Repository pattern**.

## Features

- **Add Expense/Income:** Easily record new expenses or income entries.
- **Calendar Integration:** Select dates conveniently using a calendar interface.
- **Search Functionality:** Search expenses and income records by keyword.
- **Edit & Update:** Modify existing records and update them seamlessly.
- **Delete Records:** Remove unwanted entries from your expense or income list.
- **Cache Management:** Offline support with cached data using Room Database.
- **Coroutines for Asynchronous Tasks:** Ensure smooth and efficient data operations.

## Tech Stack

- **Programming Language:** Kotlin
- **Database:** Firebase Realtime Database & Room Database
- **Architecture:** MVVM (Model-View-ViewModel)
- **Asynchronous Programming:** Coroutines
- **State Management:** LiveData
- **Design Pattern:** Repository Pattern

## Screenshots

(Add screenshots here to showcase the app's features)

## Installation

1. Clone the repository:
   ```bash
   git clone https://github.com/your-repo/expense-tracker.git
   ```

2. Open the project in Android Studio.

3. Add your Firebase configuration file (`google-services.json`) to the `app` directory.

4. Build and run the app on an emulator or a physical device.

## Code Architecture

This app follows the **MVVM architecture** for a clean and maintainable codebase.

- **Model:** Contains data models and Room entities.
- **View:** Responsible for UI elements and observing LiveData.
- **ViewModel:** Handles business logic and provides data to the UI.
- **Repository:** Manages data sources (Firebase and Room Database).

## Project Structure

```plaintext
.
├── data
│   ├── local
│   │   ├── Room Database (Entities, DAOs)
│   ├── remote
│   │   ├── Firebase Service
│   ├── repository
├── ui
│   ├── viewmodels
│   ├── activities
│   ├── fragments
├── utils
├── di
│   ├── ViewModelFactory
```

## How It Works

1. **Add New Records:** Add expenses or income entries with a date, description, and amount.
2. **Search & Edit:** Search records by keyword and edit them as needed.
3. **Offline Support:** View cached data even when offline using Room Database.
4. **Sync with Firebase:** All changes are synced to Firebase Realtime Database.

## Future Enhancements

- Add pie chart visualization for income vs expense.
- Enable monthly and yearly summaries.
- Integrate user authentication.

## Contributing

Contributions are welcome! Feel free to open issues or submit pull requests for new features or bug fixes.

## License

This project is licensed under the MIT License. See the `LICENSE` file for more details.

## Contact

For any questions or feedback, please reach out to:
- **Email:** bhobhriaaman.33@gmail.com
- **GitHub:** [your-github-username][(https://github.com/amanbhobhria/flobiz_task)]
