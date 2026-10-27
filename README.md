Twilight Forest [![Discord](https://img.shields.io/discord/313006291012288521.svg?colorB=7289DA&logo=data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAHYAAABWAgMAAABnZYq0AAAACVBMVEUAAB38%2FPz%2F%2F%2F%2Bm8P%2F9AAAAAXRSTlMAQObYZgAAAAFiS0dEAIgFHUgAAAAJcEhZcwAACxMAAAsTAQCanBgAAAAHdElNRQfhBxwQJhxy2iqrAAABoElEQVRIx7WWzdGEIAyGgcMeKMESrMJ6rILZCiiBg4eYKr%2Fd1ZAfgXFm98sJfAyGNwno3G9sLucgYGpQ4OGVRxQTREMDZjF7ILSWjoiHo1n%2BE03Aw8p7CNY5IhkYd%2F%2F6MtO3f8BNhR1QWnarCH4tr6myl0cWgUVNcfMcXACP1hKrGMt8wcAyxide7Ymcgqale7hN6846uJCkQxw6GG7h2MH4Czz3cLqD1zHu0VOXMfZjHLoYvsdd0Q7ZvsOkafJ1P4QXxrWFd14wMc60h8JKCbyQvImzlFjyGoZTKzohwWR2UzSONHhYXBQOaKKsySsahwGGDnb%2FiYPJw22sCqzirSULYy1qtHhXGbtgrM0oagBV4XiTJok3GoLoDNH8ooTmBm7ZMsbpFzi2bgPGoXWXME6XT%2BRJ4GLddxJ4PpQy7tmfoU2HPN6cKg%2BledKHBKlF8oNSt5w5g5o8eXhu1IOlpl5kGerDxIVT%2BztzKepulD8utXqpChamkzzuo7xYGk%2FkpSYuviLXun5bzdRf0Krejzqyz7Z3p0I1v2d6HmA07dofmS48njAiuMgAAAAASUVORK5CYII%3D)](https://discord.gg/6v3z26B) [![Crowdin](https://badges.crowdin.net/twilight-forest/localized.svg)](https://crowdin.com/project/twilight-forest) [![Modrinth](https://img.shields.io/modrinth/dt/twilightforest-re26?label=Modrinth&logo=modrinth)](https://modrinth.com/mod/twilightforest-re26)
==============

This is the home of the Twilight Forest mod for Minecraft 1.12 and beyond.
> **NeoForge build discontinued / NeoForge 版已停止维护**
> The NeoForge port in this branch is no longer maintained: the official Twilight Forest has been updated, so
> please use the official NeoForge build instead. This branch is kept for reference only and will not receive
> further updates.
>
> 本分支的 NeoForge 移植已停止维护：官方暮色森林已更新，请直接使用官方 NeoForge 版本；本分支仅作留档，不再更新。


## Licensing
Code unless otherwise stated default to our [LICENSE file here](LICENSE)

Non-Code, [Non-Sound](src/main/resources/assets/twilightforest/sounds) and [Non-Structure](src/main/resources/data/twilightforest/structure) assets [(Located here)](src/main/resources/assets) go by our [ASSET_LICENSE file here](ASSET_LICENSE)

Sound based assets [(Located Here)](src/main/resources/assets/twilightforest/sounds) and Structure based assets [(Located Here)](src/main/resources/data/twilightforest/structure) are sourced from the [Twilight Forest Unofficial](https://github.com/marlester-dev/twilightforest-unofficial) repository. Per its [LICENSE](https://github.com/marlester-dev/twilightforest-unofficial/blob/1.20.1-fabric/LICENSE):
- Most sound assets are derived from the Minecraft 1.7.10 version of Twilight Forest and remain under **LGPL-2.1**.
- Other sound assets (including `findings.ogg`, `home.ogg`, `maker.ogg`, `motion.ogg`, `radiance.ogg`, `steps.ogg`, `superstitious.ogg`, `thread.ogg`, `wayfarer.ogg`, and those under `mob/alpha_yeti/*`, `mob/deer/*`, `mob/mist_wolf/*`, `mob/tome/*`) are either removed (0 bytes), created by the Unofficial team under **CC BY-NC-SA 4.0**, or CC-compatible free sounds.
- Structure assets follow the same dual-licensing: code-related parts under **LGPL-2.1**, non-code assets under **CC BY-NC-SA 4.0**.

## Downloads
Download the Re26 builds from [Modrinth](https://modrinth.com/mod/twilightforest-re26).

## Community
Keep in touch with development on our [Discord server](https://discord.gg/6v3z26B). We also have a [Twitter account](https://twitter.com/teamtwilightdev) we post development and Twilight Forest-related content on.

## Translation
Submitting translations for Twilight Forest can be done via our [Crowdin page](https://crowdin.com/project/twilight-forest). Consider helping us out if you're fluent in any language at all! Alternatively, translations can still be submitted via pull requests.

## Maven
### 1.20.6+
https://maven.tamaized.com/#/releases/team-twilight/twilightforest/

```
repositories {
    maven {
        url = "https://maven.tamaized.com/releases"
    }
}

dependencies {
    implementation "team-twilight:twilightforest:{version}:universal"
}
```
### 1.20.5 and below
https://modmaven.dev/teamtwilight/twilightforest/

```
repositories {
    maven {
        url = "https://modmaven.dev/"
    }
}

dependencies {
    implementation fg.deobf("teamtwilight:twilightforest:{version}:universal")
}
```

## YourKit
![](https://www.yourkit.com/images/yklogo.png)

We appreciate YourKit, LLC for providing the project developers licenses of its profiler to help us improve performance!

YourKit supports open source projects with its full-featured Java Profiler.
YourKit, LLC is the creator of [YourKit Java Profiler](https://www.yourkit.com/java/profiler/)
and [YourKit .NET Profiler](https://www.yourkit.com/.net/profiler/),
innovative and intelligent tools for profiling Java and .NET applications.
