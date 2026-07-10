package twilightforest.mixin;

import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.systems.GpuBackend;
import net.neoforged.fml.loading.EarlyLoadingScreenController;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Window.class)
public class MixinWindow {

    @Inject(method = "createGlfwWindow", at = @At("HEAD"))
    private static void tf$detectVulkanBackend(int width, int height, String title, long monitor, GpuBackend backend, CallbackInfoReturnable<Long> cir) {
        twilightforest.client.VulkanCompatHelper.isVulkanBackend = "Vulkan".equals(backend.getName());
    }

    @Redirect(
        method = "createGlfwWindow",
        at = @At(value = "INVOKE", target = "Lnet/neoforged/fml/loading/EarlyLoadingScreenController;current()Lnet/neoforged/fml/loading/EarlyLoadingScreenController;"),
        remap = false
    )
    private static EarlyLoadingScreenController tf$skipEarlyLoadingScreenForVulkan() {
        if (twilightforest.client.VulkanCompatHelper.isVulkanBackend) {
            return null;
        }
        return EarlyLoadingScreenController.current();
    }
}
