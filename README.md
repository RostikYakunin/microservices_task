План виконання
🔹 Етап 1. Ініціалізація проєкту

Створити кореневий Gradle/Maven проєкт.

Додати два модулі: resource-service, song-service.

Налаштувати settings.gradle (або parent POM).

Додати .gitignore.

🔹 Етап 2. Інфраструктура

Написати compose.yaml з двома контейнерами PostgreSQL (окремі порти, різні БД).

Налаштувати application.yml у кожному сервісі для підключення до своєї БД.

Використати Hibernate ddl-auto=update.

🔹 Етап 3. Resource Service

Створити Entity: ResourceEntity (id, fileData).

Створити Repository (JpaRepository).

Реалізувати Service:

upload → зберігає файл у БД, читає metadata (Apache Tika), викликає Song Service.

get → повертає MP3 як byte[].

delete → видаляє за списком ID + викликає Song Service для видалення metadata.

Реалізувати DTO + Controller (slim, тільки ResponseEntity).

🔹 Етап 4. Song Service

Створити Entity: SongEntity (id, name, artist, album, duration, year).

Створити Repository (JpaRepository).

Реалізувати Service з валідацією:

create → перевірка полів + чи існує ResourceId (REST-запит у Resource Service).

get → повертає DTO.

delete → за списком ID.

Реалізувати DTO + Controller.

🔹 Етап 5. Error handling

Реалізувати @RestControllerAdvice в обох сервісах.

Повертати JSON у форматі з прикладу (errorMessage, errorCode, details).

🔹 Етап 6. Тестування API

Запустити обидва сервіси локально.

Підняти PostgreSQL через docker-compose up -d.

Імпортувати Postman collection.

Запустити всі тести → перевірити, щоб пройшли 100%.

Зробити скріни → скласти PDF.

🔹 Етап 7. Фіналізація

Завантажити код у GitHub (публічний репо).

В особистій папці Avalia:

Додати лінк на репо.

Завантажити PDF з тестами.