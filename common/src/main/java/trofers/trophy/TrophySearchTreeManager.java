package trofers.trophy;

import net.minecraft.ChatFormatting;
import net.minecraft.client.searchtree.FullTextSearchTree;
import net.minecraft.client.searchtree.RefreshableSearchTree;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import trofers.block.TrophyBlock;
import trofers.registry.ModResourceLoaders;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TrophySearchTreeManager implements ResourceManagerReloadListener {

    private static RefreshableSearchTree<ResourceLocation> searchTree;

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
                        ChatFormatting.stripFormatting(ModResourceLoaders.TROPHIES.get(trophyId).name()
                                .orElse(Component.translatable(TrophyBlock.DESCRIPTION_ID))
                                .getString()
                        ).trim()
                ),
                Stream::of,
                ModResourceLoaders.TROPHIES.keys()
                        .stream()
                        .filter(trophyId -> !ModResourceLoaders.TROPHIES.get(trophyId).isHidden())
                        .sorted(Comparator.comparing(ResourceLocation::toString))
                        .collect(Collectors.toList())
        );

        searchTree.refresh();
    }
}
