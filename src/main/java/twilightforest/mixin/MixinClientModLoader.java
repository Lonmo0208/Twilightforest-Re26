package twilightforest.mixin;

import net.neoforged.fml.earlydisplay.DisplayWindow;
import net.neoforged.neoforge.client.loading.ClientModLoader;
import org.lwjgl.glfw.GLFW;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import twilightforest.client.VulkanCompatHelper;

import java.util.concurrent.ScheduledExecutorService;

@Mixin(ClientModLoader.class)
public class MixinClientModLoader {

    @Redirect(
        method = "finish",
        at = @At(
            value = "INVOKE",
            target = "Lnet/neoforged/fml/earlydisplay/DisplayWindow;close()V",
            ordinal = 0
        ),
        remap = false
    )
    private static void tf$skipDisplayWindowCloseForVulkan(DisplayWindow displayWindow) {
        if (VulkanCompatHelper.isVulkanBackend) {
            try {
                var windowField = DisplayWindow.class.getDeclaredField("window");
                windowField.setAccessible(true);
                long handle = windowField.getLong(displayWindow);
                if (handle != 0L) {
                    GLFW.glfwHideWindow(handle);
                    GLFW.glfwDestroyWindow(handle);
                }

                var closedField = DisplayWindow.class.getDeclaredField("closed");
                closedField.setAccessible(true);
                closedField.setBoolean(displayWindow, true);

                var schedulerField = DisplayWindow.class.getDeclaredField("renderScheduler");
                schedulerField.setAccessible(true);
                var scheduler = (ScheduledExecutorService) schedulerField.get(displayWindow);
                scheduler.shutdown();
            } catch (Exception ignored) {
            }
            return;
        }

        displayWindow.close();
    }
}
