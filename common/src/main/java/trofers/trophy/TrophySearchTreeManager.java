package trofers.trophy;

import net.minecraft.ChatFormatting;
import net.minecraft.client.searchtree.FullTextSearchTree;
import net.minecraft.client.searchtree.RefreshableSearchTree;
import net.minecraft.network.chat.Component;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import trofers.block.TrophyBlock;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TrophySearchTreeManager implements ResourceManagerReloadListener {

    private static RefreshableSearchTree<Trophy> searchTree;

    @Override
    public void onResourceManagerReload(ResourceManager manager) {
        createSearchTree();
    }

    public static List<Trophy> search(String text) {
        return searchTree.search(text);
    }

    @SuppressWarnings("ConstantConditions")
    public static void createSearchTree() {
        searchTree = new FullTextSearchTree<>(
                trophy -> Stream.of(
                        ChatFormatting.stripFormatting(trophy.name()
                                .orElse(Component.translatable(TrophyBlock.DESCRIPTION_ID))
                                .getString()
                        ).trim()
                ),
                trophy -> Stream.of(trophy.id()),
                TrophyManager.values()
                        .stream()
                        .filter(trophy -> !trophy.isHidden())
                        .sorted(Comparator.comparing(trophy -> trophy.id().toString()))
                        .collect(Collectors.toList())
        );

        searchTree.refresh();
    }
}
