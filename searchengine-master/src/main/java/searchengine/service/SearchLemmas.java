package searchengine.service;

import lombok.RequiredArgsConstructor;
import org.apache.lucene.morphology.LuceneMorphology;
import org.apache.lucene.morphology.russian.RussianLuceneMorphology;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.*;

@RequiredArgsConstructor
public class SearchLemmas {

    private final LuceneMorphology luceneMorph;
    private static final String WORD_TYPE_REGEX = "\\W\\w&&[^а-яА-Я\\s]";
    private static final String[] PARTICLES_NAMES = new String[]{"МЕЖД", "ПРЕДЛ", "СОЮЗ"};
    private static final String HTML = "html";
    private static final String WORD_REGEX = "([^а-я\\s])";
    private static final String SPACE_REGEX = "\\s+";

    public static SearchLemmas getLuceneMorphology() throws IOException {
        LuceneMorphology luceneMorph = new RussianLuceneMorphology();
        return new SearchLemmas(luceneMorph);
    }

    public Map<String, Integer> lemma(Document document) {
        HashMap<String, Integer> lemmas = new HashMap<>();
        String[] text = parseText(document);
        Arrays.stream(text)
                .map(this::baseFormLemma)
                .filter(Objects::nonNull)
                .forEach(lemma -> {
                    if (lemmas.containsKey(lemma))
                        lemmas.put(lemma, lemmas.get(lemma) + 1);
                    else
                        lemmas.put(lemma, 1);
                });
        return lemmas;
    }

    public String baseFormLemma(String lemma) {

        if (lemma.isBlank()) {
            return null;
        }
        List<String> wordBaseFormsInfo = luceneMorph.getMorphInfo(lemma);
        if (checkServiceUnits(wordBaseFormsInfo.get(0))) {
            return null;
        }
        List<String> wordBaseForms = luceneMorph.getNormalForms(lemma);
        if (wordBaseForms.get(0).matches(WORD_TYPE_REGEX)) {
            return null;
        }
        return wordBaseForms.get(0);
    }

    private boolean checkServiceUnits(String lemma) {
        for (String name : PARTICLES_NAMES) {
            if (lemma.contains(name)) {
                return true;
            }
        }
        return false;
    }

    private String[] parseText(Document document) {
        String documentElements = document.getElementsByTag(HTML).text();
        return documentElements.toLowerCase(Locale.ROOT)
                .replaceAll(WORD_REGEX, " ")
                .trim()
                .split(SPACE_REGEX);
    }
}
