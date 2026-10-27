Twilight Forest Re:26 [![Modrinth](https://img.shields.io/modrinth/dt/twilightforest-re26?label=Modrinth&logo=modrinth)](https://modrinth.com/mod/twilightforest-re26)
==============

This is the home of the Twilight Forest mod for Minecraft 1.12 and beyond.

> **Note about this repository / 关于本仓库**
> Besides the upstream sources, this repository hosts the **Re26 port** for Minecraft 26.x for Fabric.
> **The NeoForge port is discontinued**: the official Twilight Forest has been updated, so please use the official
> NeoForge build. The NeoForge branches (`26.1.x`, `26.2.x`, `26w14a`) are kept for reference only.
> **The 26.1.2 port is archived** as well: the official Twilight Forest now supports 26.1.2, so use the official
> release for that version.
>
> 本仓库除上游源码外，还维护面向 Minecraft 26.x 的 **Re26 移植（Fabric）**。
> **NeoForge 版已停止维护**：官方暮色森林已更新，请直接使用官方 NeoForge 版本；
> 对应分支（`26.1.x`、`26.2.x`、`26w14a`）仅作留档。
> 此外 **26.1.2 的移植已归档**：官方暮色森林已支持 26.1.2，请直接使用官方版本。

## Version support / 版本支持

| Minecraft | Branch | Status |
| --- | --- | --- |
| 26.3 | `26.3.x-Fabric` (default) | Active - Fabric |
| 26.4-snapshot-6 | `26.4/dev` | Dev - Fabric |
| 26.2 | `26.2.x-Fabric` | Active - Fabric |
| 26w14a | `26w14a-Fabric` | Snapshot port - Fabric |
| 26.1.2 | `26.1.x-Fabric` | **Archived** - superseded by the official Twilight Forest 26.1.2 release |
| 26.1.2 / 26.2 / 26w14a | `26.1.x`, `26.2.x`, `26w14a` | **NeoForge: discontinued** - use the official NeoForge build instead |

## Licensing
Code unless otherwise stated default to our [LICENSE file here](LICENSE)

Non-Code, [Non-Sound](src/main/resources/assets/twilightforest/sounds) and [Non-Structure](src/main/resources/data/twilightforest/structure) assets [(Located here)](src/main/resources/assets) go by our [ASSET_LICENSE file here](ASSET_LICENSE)

Sound based assets [(Located Here)](src/main/resources/assets/twilightforest/sounds) and Structure based assets [(Located Here)](src/main/resources/data/twilightforest/structure) are sourced from the [Twilight Forest Unofficial](https://github.com/marlester-dev/twilightforest-unofficial) repository. Per its [LICENSE](https://github.com/marlester-dev/twilightforest-unofficial/blob/1.20.1-fabric/LICENSE):
- Most sound assets are derived from the Minecraft 1.7.10 version of Twilight Forest and remain under **LGPL-2.1**.
- Other sound assets (including `findings.ogg`, `home.ogg`, `maker.ogg`, `motion.ogg`, `radiance.ogg`, `steps.ogg`, `superstitious.ogg`, `thread.ogg`, `wayfarer.ogg`, and those under `mob/alpha_yeti/*`, `mob/deer/*`, `mob/mist_wolf/*`, `mob/tome/*`) are either removed (0 bytes), created by the Unofficial team under **CC BY-NC-SA 4.0**, or CC-compatible free sounds.
- Structure assets follow the same dual-licensing: code-related parts under **LGPL-2.1**, non-code assets under **CC BY-NC-SA 4.0**.

## Downloads
Download the Re26 builds from [Modrinth](https://modrinth.com/mod/twilightforest-re26).

## Translation
Translations can be submitted via pull requests.

## Maven
The builds are published on Modrinth, which also provides a Maven repository:
https://modrinth.com/mod/twilightforest-re26/versions

```gradle
repositories {
    maven {
        url = "https://api.modrinth.com/maven"
    }
}

dependencies {
    modImplementation "maven.modrinth:twilightforest-re26:{version}"
}
```

Use the version number shown on the Modrinth versions page. If that number is shared by several Minecraft versions (for example `Fabric-4.8.4210` exists for both 26.2 and 26.3), use the version ID shown on the version page instead; Modrinth Maven accepts either.

## YourKit
![](https://www.yourkit.com/images/yklogo.png)

We appreciate YourKit, LLC for providing the project developers licenses of its profiler to help us improve performance!

YourKit supports open source projects with its full-featured Java Profiler.
YourKit, LLC is the creator of [YourKit Java Profiler](https://www.yourkit.com/java/profiler/)
and [YourKit .NET Profiler](https://www.yourkit.com/.net/profiler/),
innovative and intelligent tools for profiling Java and .NET applications.
