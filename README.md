# EN:
EstherProject Client for Android. EstherProject is sample forum on Ethereum blockchain.

# RU:
В этом репозитории размещен исходый код клиента EstherProject для Android.

## Информация
- Приложение использует базу данных RealmDB.
- Данные хранятся в блокчейне Ethereum (тестовая сеть Rinkeby).
- Пользователь может создавать новые темы, писать новые посты к темам, а также выбрать юзернейм и сделать резервную копию приватного ключа.

## Branches (master, v1.0, v1.1, v1.2 и v1.3)
* master - Последние изменения.
* v1.3 - С этой версии используется WorkManager для обновления информации, когда приложение не используется. 
* v1.2 - С этой версии используется Dagger 2. Также добавлена автоматическая проверка баланса, в случае изменения баланса выводится уведомление.
* v1.1 - С этой версии приложение основано на паттерне MVP.
* v1.0 - Первая версия приложения.

## Скриншоты
Начальный экран:

<img src="/screenshots/1.png?raw=true" width="360" height="640">

Список тем:

<img src="/screenshots/2.png?raw=true" width="360" height="640">

Данные о аккаунте:

<img src="/screenshots/3.png?raw=true" width="360" height="640">

Форма добавления новой темы:

<img src="/screenshots/4.png?raw=true" width="360" height="640">

Открытая тема с комментариями:

<img src="/screenshots/5.png?raw=true" width="360" height="640">

Открытая тема без комментариев:

<img src="/screenshots/6.png?raw=true" width="360" height="640">

Форма добавления постов:

<img src="/screenshots/8.png?raw=true" width="360" height="640">