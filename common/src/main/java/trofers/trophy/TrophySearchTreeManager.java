package trofers.trophy;

import net.minecraft.ChatFormatting;
import net.minecraft.client.searchtree.FullTextSearchTree;
import net.minecraft.client.searchtree.SearchTree;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import trofers.block.TrophyBlock;
import trofers.registry.ModRegistries;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TrophySearchTreeManager implements ResourceManagerReloadListener {

    private static SearchTree<ResourceLocation> searchTree;

    @Override
    public void onResourceManagerReload(ResourceManager manager) {
        createSearchTree();
    }

    public static List<ResourceLocation> search(String text) {
        return searchTree.search(text);
    }

    @SuppressWarnings("ConstantConditions")
    public static void createSearchTree() {
        searchTree = new FullTextSearchTree<>(
                trophyId -> Stream.of(
                        ChatFormatting.stripFormatting(ModRegistries.trophies().get(trophyId).name()
                                .orElse(Component.translatable(TrophyBlock.DESCRIPTION_ID))
                                .getString()
                        ).trim()
                ),
                Stream::of,
                ModRegistries.trophies() == null ? List.of() : ModRegistries.trophies().keySet()
                        .stream()
                        .filter(trophyId -> !ModRegistries.trophies().get(trophyId).isHidden())
                        .sorted(Comparator.comparing(ResourceLocation::toString))
                        .collect(Collectors.toList())
        );
    }
}
