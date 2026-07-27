package twilightforest.init;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Util;
import net.minecraft.world.item.JukeboxSong;
import twilightforest.TwilightForestMod;

public class TFJukeboxSongs {

	public static final ResourceKey<JukeboxSong> RADIANCE = registerKey("radiance");
	public static final ResourceKey<JukeboxSong> STEPS = registerKey("steps");
	public static final ResourceKey<JukeboxSong> SUPERSTITIOUS = registerKey("superstitious");
	public static final ResourceKey<JukeboxSong> HOME = registerKey("home");
	public static final ResourceKey<JukeboxSong> WAYFARER = registerKey("warfarer");
	public static final ResourceKey<JukeboxSong> FINDINGS = registerKey("findings");
	public static final ResourceKey<JukeboxSong> MAKER = registerKey("maker");
	public static final ResourceKey<JukeboxSong> THREAD = registerKey("thread");
	public static final ResourceKey<JukeboxSong> MOTION = registerKey("motion");

	private static ResourceKey<JukeboxSong> registerKey(String name) {
		return ResourceKey.create(Registries.JUKEBOX_SONG, TwilightForestMod.prefix(name));
	}

	public static void init() {
		// JukeboxSongs are registered via datapack JSON files (data/twilightforest/jukebox_song/)
	}

	public static void bootstrap(BootstrapContext<JukeboxSong> context) {
		registerSong(context, RADIANCE, TFSounds.MUSIC_DISC_RADIANCE, 122);
		registerSong(context, STEPS, TFSounds.MUSIC_DISC_STEPS, 140);
		registerSong(context, SUPERSTITIOUS, TFSounds.MUSIC_DISC_SUPERSTITIOUS, 160);
		registerSong(context, HOME, TFSounds.MUSIC_DISC_HOME, 157);
		registerSong(context, WAYFARER, TFSounds.MUSIC_DISC_WAYFARER, 176);
		registerSong(context, FINDINGS, TFSounds.MUSIC_DISC_FINDINGS, 178);
		registerSong(context, MAKER, TFSounds.MUSIC_DISC_MAKER, 175);
		registerSong(context, THREAD, TFSounds.MUSIC_DISC_THREAD, 189);
		registerSong(context, MOTION, TFSounds.MUSIC_DISC_MOTION, 200);
	}

	private static void registerSong(BootstrapContext<JukeboxSong> context, ResourceKey<JukeboxSong> key, SoundEvent soundEvent, int lengthInSeconds) {
		context.register(key, new JukeboxSong(
			Holder.direct(soundEvent),
			Component.translatable(Util.makeDescriptionId("jukebox_song", key.identifier())),
			lengthInSeconds,
			0
		));
	}
}
