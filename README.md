# Gorillas
Gorillas was the final project for the Intro To Computer science class (GDI1) in the Winter Semester 2014 (WS2014) at Tu Darmstadt
Gorillas is a remake of and old [dos game](https://en.wikipedia.org/wiki/Gorillas_(video_game))


## Major Features
- Procedural Map Generation
- Destructible Environment
- Dynamic Action Cam (Zoom, Movement, SlowMo)
- SQL Based Highscore System
- Animations, Sounds, Different Projectiles and more...


## Screenshots

<img src="doc/screenshots/Screenshot_MainMenue.png" width="30%"></img> <img src="doc/screenshots/Screenshot_Options.png" width="30%"></img> <img src="doc/screenshots/Screenshot_SlowMo.png" width="30%"></img>

<img src="doc/screenshots/Screenshot_Sun.png" width="30%"></img> <img src="doc/screenshots/Screenshot_Explosion.png" width="30%"></img> <img src="doc/screenshots/Screenshot_Treffer.png" width="30%"></img>


## Implementation Overview
We went for an hybrid entity component based implementation, with a small inheritance chain.

### Game State Machine
![Game State Machine](doc/diagrams/diagram_fsm.png)

### Game Architecture
![Game Architecture Overview](doc/diagrams/diagram_overview_white.png)


## Frameworks
[Slick2D] (http://slick.ninjacave.com/)
[LwJGL] (http://www.lwjgl.org/)
[SQLite JDBC Driver] (https://github.com/xerial/sqlite-jdbc)
[EEA Framework] (https://www.linkedin.com/in/guidoroessling)


## Disclaimer
Published under the [MIT License](LICENSE.md).