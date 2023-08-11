Template repository for ExploreWithMe project.
https://github.com/PVD777/java-explore-with-me/pull/5


Свободное время является ценным ресурсом, который мы ежедневно планируем использовать, решая, 
куда пойти и с кем провести его. Однако, самым сложным в этом процессе является поиск информации 
и организация встреч. Нам приходится задумываться о предстоящих мероприятиях, доступности друзей 
в данный момент, а также о том, как всех пригласить и где встретиться. Для упрощения этого процесса 
было создано приложение-афиша, которое позволяет предложить различные события, начиная от выставок 
и заканчивая походами в кино, и найти компанию для их посещения. 
Ниже приведен пример веб-интерфейса данного приложения.

<picture>
    <img src="ewm-service/src/main/resources/app.png">
</picture>

## Архитектура приложения:wrench:

Приложение включает в себя два микросервиса ewm-service и statistics-service. Сервис statistics-service 
содержит три модуля client, dto и server и обращается к собственной базе данных. 
Модуль client является шлюзом между основным модудем и модулем статистики. 
Основной сервис ewm-service содержит все необходимые компоненты для работы продукта, 
а хранение данных происходит в базе данных. 

## Функционал приложения:paperclip:

**Основной сервис** включает в себя функции, распределенные по категориям доступа:

1) **Администраторы**:cop::

    - управление категориями для событий - добавлять, измененять и удалять категории.
    - управление подборками мероприятий - добавлять, удалять и закреплять на главной странице.
    - модерирование события, размещённые пользователями, — публикация или отклонение.
    - управление пользователями — добавлять, просматривать и удалять.
    - получение подробной информации об всех ивентах указанного события.

2) **Авторизованные пользователи**:family::

    - добавление, редактирование и просмотр мероприятий.
    - оформление заявки на участие в интересующих мероприятиях.
    - подтверждкние заявки на свое мероприятие другим пользователям .
    - получать краткую информацию обо всех ивентах своего мероприятиях.

3) **Незарегистрированные пользователи**:busts_in_silhouette::

    - просмотр списка событий согласно параметрам с краткой информацией по ним
    - просмотр подробной информации о выбранном событии.
    - просмотр всей имеющейся категории событий.
    - просмотр подборки событий, составленной админом.

   **Сервис статистики**:chart_with_upwards_trend::

    - сбор информацию о количестве обращений пользователей к спискам событий.
    - сбор информацию о количестве запросов к подробной информации о событии. 
    - подтверждение заявки на свое мероприятие другим пользователям. 
 

## Спецификация:books:
Спецификацию для сервисов проекта можно посмотреть на сайте Swagger https:// editor-next.swagger.io, 
импортировав туда файлы из папки проекта: ewm-main-service-spec.json для основного сервиса 
и ewm-stats-service-spec.json для сервиса статистики.