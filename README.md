# Scrambler
Scrambler is a fun text game for your Minecraft server! It allows you to reward players for having fast typing skills and being engaged in chat.

##Config.YML
```YAML
Plugin Prefix: '&8[&5Scrambler&8]'
Interval: 60
words:
  Minimum Length: 8
  Maximum Length: 12
rewards:
  Reward Command: '%SERVER% give {player} diamond 12'

```
```
>Plugin Prefix - The prefix before all messages in the plugin
>Interval - Amount of seconds between automatically starting a Scrambler Game
>Minimum Length - The Minimum length of the word generated for the Scrambler Game
>Maximum Length - The Maximum length of the word generated for the Scrambler Game
>Reward Command - Command that is executed on completion of Game
```
######Reward Commands PlaceHolders
```
> %SERVER% - Server Command
> %PLAYER% - Player Command
> {player} - Winning Player
```


##Permissions
```YAML
  scrambler.*:
    description: Access all commands in Scrambler
    default: op
  scrambler.pause:
    description: Pause Scrambler timer
    default: op
  scrambler.start:
    description: Start Scrambler timer
    default: op
  scrambler.forcestop:
    description: Stop a running Scrambler game
    default: op
  scrambler.frocestart:
    description: Force start a Scrambler game
    default: op
```