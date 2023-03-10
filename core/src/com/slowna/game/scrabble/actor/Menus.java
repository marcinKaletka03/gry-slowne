package com.slowna.game.scrabble.actor;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.slowna.game.App;
import com.slowna.game.MyGdxGame;
import com.slowna.game.asset.Musics;
import com.slowna.game.asset.Textures;
import com.slowna.game.gameData.AppLanguage;
import com.slowna.game.scrabble.ScrabbleGame;
import com.slowna.game.scrabble.bot.BotDifficult;
import com.slowna.game.scrabble.extra.MenuTittle;

import java.util.List;

public enum Menus {
    START_MENUS {
        @Override
        public MenuTittle[] getTitles() {
            return new MenuTittle[]{
                    new MenuTittle() {
                        @Override
                        public String getName(AppLanguage language) {
                            return App.lang().startGame;
                        }

                        @Override
                        public Runnable onClick(ScrabbleMenu menu, MyGdxGame game) {
                            return () -> menu.updateMenu(START_GAME.getTitles());
                        }
                    },
                    new MenuTittle() {
                        @Override
                        public String getName(AppLanguage language) {
                            return App.lang().high_scores;
                        }

                        @Override
                        public Runnable onClick(ScrabbleMenu menu, MyGdxGame game) {
                            return () -> menu.updateMenu(HIGH_SCORES.getTitles());
                        }
                    },
                    new MenuTittle() {
                        @Override
                        public String getName(AppLanguage language) {
                            return App.lang().settings;
                        }

                        @Override
                        public Runnable onClick(ScrabbleMenu menu, MyGdxGame game) {
                            return () -> menu.updateMenu(OPTIONS.getTitles());
                        }
                    },
                    new MenuTittle() {
                        @Override
                        public String getName(AppLanguage language) {
                            return App.lang().exit;
                        }

                        @Override
                        public Runnable onClick(ScrabbleMenu menu, MyGdxGame game) {
                            return () -> menu.clearMenu(() -> {
                                Gdx.app.exit();
                                System.exit(0);
                            });
                        }
                    },
            };
        }
    },

    START_GAME {
        @Override
        public MenuTittle[] getTitles() {
            MenuTittle[] tittles = new MenuTittle[4];
            int counter = 0;
            for (BotDifficult botDifficult : BotDifficult.values()) {
                tittles[counter++] = new MenuTittle() {
                    @Override
                    public String getName(AppLanguage language) {
                        return botDifficult.getName();
                    }

                    @Override
                    public Runnable onClick(ScrabbleMenu menu, MyGdxGame game) {
                        return () -> menu.clearMenu(() -> ScrabbleGame.play(botDifficult));
                    }

                    @Override
                    public TextureRegion getTexture() {
                        if (botDifficult == BotDifficult.EASY) {
                            return new TextureRegion(Textures.GREEN_S.get());
                        } else if (botDifficult == BotDifficult.HARD) {
                            return new TextureRegion(Textures.RED_S.get());
                        }
                        return MenuTittle.super.getTexture();
                    }
                };

            }
            tittles[3] = new MenuTittle() {
                @Override
                public String getName(AppLanguage language) {
                    return App.lang().back;
                }

                @Override
                public Runnable onClick(ScrabbleMenu menu, MyGdxGame game) {
                    return () -> menu.updateMenu(START_MENUS.getTitles());
                }
            };
            return tittles;
        }
    },

    OPTIONS {
        @Override
        public MenuTittle[] getTitles() {
            AppLanguage[] values = AppLanguage.values();
            MenuTittle[] result = new MenuTittle[values.length + 2];
            for (int i = 0; i < values.length; i++) {
                AppLanguage language = values[i];
                result[i] = new MenuTittle() {
                    @Override
                    public String getName(AppLanguage lg) {
                        return language.languageName;
                    }

                    @Override
                    public Runnable onClick(ScrabbleMenu menu, MyGdxGame game) {
                        return () -> {
                            App.prefs().setLanguage(language);
                            menu.updateMenu(START_MENUS.getTitles());
                        };
                    }
                };
            }

            result[result.length - 2] = new MenuTittle() {

                final PlayerData playerData = App.prefs().getPlayerData();
                final boolean soundIsOn = playerData.isSoundOn();

                @Override
                public String getName(AppLanguage language) {
                    return soundIsOn ? language.soundOff : language.soundOn;
                }

                @Override
                public Runnable onClick(ScrabbleMenu menu, MyGdxGame game) {
                    return () -> {
                        playerData.setSoundOn(!soundIsOn);
                        if (soundIsOn) {
                            App.musicManager().stopCurrent();
                        } else {
                            App.musicManager().playMusic(Musics.THEME);
                        }
                        App.prefs().saveData();
                        menu.updateMenu(OPTIONS.getTitles());
                    };
                }
            };

            result[result.length - 1] = new MenuTittle() {
                @Override
                public String getName(AppLanguage language) {
                    return App.lang().back;
                }

                @Override
                public Runnable onClick(ScrabbleMenu menu, MyGdxGame game) {
                    return () -> menu.updateMenu(START_MENUS.getTitles());
                }
            };
            return result;
        }
    },
    HIGH_SCORES() {
        @Override
        public MenuTittle[] getTitles() {
            List<Integer> scores = App.prefs().getPlayerData().getPoints();

            MenuTittle[] titles = new MenuTittle[scores.size() + 1];

            for (int i = 0; i < scores.size(); i++) {
                Integer score = scores.get(i);
                titles[i] = new MenuTittle() {
                    @Override
                    public String getName(AppLanguage language) {
                        return score + " points";
                    }

                    @Override
                    public Runnable onClick(ScrabbleMenu menu, MyGdxGame game) {
                        return () -> {
                        };
                    }
                };
            }

            titles[titles.length - 1] = new MenuTittle() {

                @Override
                public String getName(AppLanguage language) {
                    return language.back;
                }

                @Override
                public Runnable onClick(ScrabbleMenu menu, MyGdxGame game) {
                    return () -> menu.updateMenu(START_MENUS.getTitles());
                }
            };
            return titles;
        }
    };

    public abstract MenuTittle[] getTitles();

    @Override
    public String toString() {
        return name();
    }
}
