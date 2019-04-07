package aunmag.shooter.game.client.states;

import aunmag.shooter.core.gui.font.FontStyle;
import aunmag.shooter.core.structures.Texture;
import aunmag.shooter.core.gui.Button;
import aunmag.shooter.core.gui.Label;
import aunmag.shooter.core.gui.Page;
import aunmag.shooter.game.client.App;
import aunmag.shooter.game.environment.actor.ActorType;

public class SettingsPage {
    // Массив с объектами кнопок выбора персонажа
    private final Button[] actorSelectButtons = {
        // Классический
        new Button(5, 2, 1, 1, "Classic", () -> {
            changeActorOrAccept(0,
                    ScenarioStatus.scenarioEncircling,
                    ActorType.human);
        }),
        // Ковбой
        new Button(7, 2, 1, 1, "Cowboy", () -> {
            changeActorOrAccept(1,
                    ScenarioStatus.scenarioEncircling,
                    ActorType.humanCowboy);
        }),
    };

    // Конструктор
    public SettingsPage() {
        // Автоматически делаем кнопку выбора первого персонажа нажатой
        actorSelectButtons[0].setEnabled(false);
    }

    // Создаёт и возвращает объект собственно страницы настроек
    public Page createPageSettings() {
        Texture wallpaper = Texture.getOrCreate(
                "images/wallpapers/main_menu", Texture.Type.WALLPAPER
        );
        Page page = new Page(wallpaper);
        FontStyle style = FontStyle.LABEL_LIGHT;

        // Название раздела
        page.add(new Label(5, 1, 2, 1, "Settings"));
        // Выбор игрока
        page.add(new Label(3, 2, 1, 1, "Select Player", style));
        // Выводим все кнопки выбора игроков
        for (Button button : actorSelectButtons) {
            page.add(button);
        }

        // Выход в главное меню
        page.add(new Button(4, 10, 4, 1, "Back", Button.ACTION_BACK));

        return page;
    }

    // По мере необходимости либо сразу меняет актёра,
    //   либо открывает страницу подтверждения смены
    public void changeActorOrAccept(
            // Индекс кнопки из массива `actorSelectButtons`,
            //   которая станент недоступна
            int indexOfUnavaliableButton,
            ScenarioStatus scenarioStatus,
            ActorType newActorType
    ) {
        if (App.main.getGame() != null) {
            createActorChangeAcceptingPage(
                    indexOfUnavaliableButton,
                    scenarioStatus,
                    newActorType
            ).open();
        } else {
            changeActor(scenarioStatus, newActorType);
            setUnavaliableButton(indexOfUnavaliableButton);
        }
    }

    // Создаёт и возвращает объект окна
    //   утверждения смены персонажа со сбросом текущей игры
    public Page createActorChangeAcceptingPage(
            // Индекс кнопки из массива `actorSelectButtons`,
            //   которая станент недоступна
            int indexOfUnavaliableButton,
            ScenarioStatus scenarioStatus,
            ActorType newType
    ) {
        Texture wallpaper = Texture.getOrCreate(
                "images/wallpapers/main_menu", Texture.Type.WALLPAPER
        );
        Page page = new Page(wallpaper);
        page.add(new Label(5, 1, 1, 1,
                "When you change the character, the current game will be lost"));
        page.add(new Label(5, 2, 1, 1, "Continue?"));

        page.add(new Button(4, 4, 2, 1, "Yes", () -> {
            App.main.endGame();
            changeActor(scenarioStatus, newType);
            setUnavaliableButton(indexOfUnavaliableButton);
            Button.ACTION_BACK.run();
        }));
        page.add(new Button(6, 4, 2, 1, "No", Button.ACTION_BACK));

        return page;
    }

    // Функция, меняющая персонаж игрока и объект прицеливания
    //   в переданном сценарии
    private void changeActor(ScenarioStatus scenarioStatus, ActorType newType) {
        scenarioStatus.actorType = newType;
        // Если в данный момент существует объект игры:
        if (App.main.getGame() != null) {
            Game game = App.main.getGame();
            game.getPlayer().getActor().setType(newType);
        }
    }

    // Задание кнопки, которая выбрана(единственная недоступная для нажатия),
    //   по индексу её нахождения в массиве всех кнопок
    public void setUnavaliableButton(int index) {
        for (int i = 0; i < actorSelectButtons.length; i++) {
            Button curButton = actorSelectButtons[i];
            if (index != i) {
                curButton.setEnabled(true);
            } else {
                curButton.setEnabled(false);
            }
        }
    }
}
