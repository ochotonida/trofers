package trofers.trophy;

import net.minecraft.ChatFormatting;
import net.minecraft.client.searchtree.FullTextSearchTree;
import net.minecraft.client.searchtree.SearchTree;
import net.minecraft.core.Registry;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import trofers.Trofers;
import trofers.block.TrophyBlock;
import trofers.registry.ModRegistries;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
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

    public static void createSearchTree() {
        if (ModRegistries.trophies().isEmpty()) {
            Trofers.LOGGER.warn("Failed to create trophy search tree, registry not found");
            return;
        }
        searchTree = new FullTextSearchTree<>(
                TrophySearchTreeManager::getNames,
                Stream::of, // searching by id doesn't make a lot of sense for data driven stuff
                getTrophies()
        );
    }

    private static Trophy getTrophy(ResourceLocation trophyId) {
        return Objects.requireNonNull(ModRegistries.trophies().orElseThrow().get(trophyId));
    }

    private static Stream<String> getNames(ResourceLocation trophyId) {
        return Stream.of(
                ChatFormatting.stripFormatting(getTrophy(trophyId).name()
                        .orElse(Component.translatable(TrophyBlock.DESCRIPTION_ID))
                        .getString()
                ).trim()
        );
    }

    private static List<ResourceLocation> getTrophies() {
        Registry<Trophy> trophies = ModRegistries.trophies().orElseThrow();
        return trophies.keySet()
                .stream()
                .filter(trophyId -> !Objects.requireNonNull(trophies.get(trophyId)).isHidden())
                .sorted(Comparator.comparing(ResourceLocation::toString))
                .collect(Collectors.toList());
    }
}
