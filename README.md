# RobotCode2020

Crevolution's robot code for FIRST's 2020 game Infinite Recharge

## Robot

### Subsystems

| Subsystem | Components | Programmer |
| --- | --- | --- |
| Drivetrain | Drivebase, Gear Shifter | JD |
| Intake | Motor, Extender | Parker |
| Indexer/Hopper | Snail, Elevator | |
| Shooter | Turret, Angler, Shooter | Ryan B |
| Climber | ClimberComponent | John |
| Disker | DiskerComponent | Ryan A |

### Hardware

| Component | Motors | Pneumatics | Sensors |
| --- | --- | --- | --- |
| Drivebase | 6 Spark MAXs |  | 2 Encoders |
| Gear Shifter | | 1 | |
| Intake Motor | 1 VictorSPX | | |
| Intake Extender | | 1 | |
| Snail | 1 or 2 VictorSPXs | | |
| Elevator | 1 VictorSPX | | |
| Turret | 1 TalonSRX | | 1 Encoder |
| Angler | 1 TalonSRX | | 1 Encoder |
| Shooter | 2 or 3 TalonSRXs | | 1 Encoder |
| Climber | 1 VictorSPX | | |
| Disker | 1 VictorSPX | | 1 Encoder, 1 Color Sensor |

## Contributing

Aim to have your code be:

1. Effective
2. Readable
3. Intuitive
4. Minimal

If it works, that's what matters. That's what the simulator's for. Everything else should come after (or, preferably, during)

Only use patterns (mVariableName, dependency injection, singletons) where it helps. Everything will be homogenized eventually.
