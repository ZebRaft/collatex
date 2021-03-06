/*
 * Copyright (c) 2015 The Interedition Development Group.
 *
 * This file is part of CollateX.
 *
 * CollateX is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * CollateX is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with CollateX.  If not, see <http://www.gnu.org/licenses/>.
 */
package eu.interedition.collatex.dekker;

import eu.interedition.collatex.Token;
import eu.interedition.collatex.VariantGraph;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

/**
 * @author Ronald Haentjens Dekker
 * @author Bram Buitendijk
 */
public class PhraseMatchDetector {

    public List<List<Match>> detect(Map<Token, VariantGraph.Vertex> linkedTokens, VariantGraph base, Iterable<Token> tokens) {
        List<List<Match>> phraseMatches = new ArrayList<>();
        List<VariantGraph.Vertex> basePhrase = new ArrayList<>();
        List<Token> witnessPhrase = new ArrayList<>();
        VariantGraph.Vertex previous = base.getStart();

        for (Token token : tokens) {
            if (!linkedTokens.containsKey(token)) {
                addNewPhraseMatchAndClearBuffer(phraseMatches, basePhrase, witnessPhrase);
                continue;
            }
            VariantGraph.Vertex baseVertex = linkedTokens.get(token);
            // requirements:
            // - previous and base vertex should have the same witnesses
            // - previous and base vertex should either be in the same transposition(s) or both aren't in any transpositions
            // - there should be a directed edge between previous and base vertex
            // - there may not be a longer path between previous and base vertex
            boolean sameTranspositions = new HashSet<>(previous.transpositions()).equals(new HashSet<>(baseVertex.transpositions()));
            boolean sameWitnesses = previous.witnesses().equals(baseVertex.witnesses());
            boolean directedEdge = previous.outgoing().containsKey(baseVertex);
            boolean isNear = sameTranspositions && sameWitnesses && directedEdge && (previous.outgoing().size() == 1 || baseVertex.incoming().size() == 1);
            if (!isNear) {
                addNewPhraseMatchAndClearBuffer(phraseMatches, basePhrase, witnessPhrase);
            }
            basePhrase.add(baseVertex);
            witnessPhrase.add(token);
            previous = baseVertex;
        }
        if (!basePhrase.isEmpty()) {
            phraseMatches.add(Match.createPhraseMatch(basePhrase, witnessPhrase));
        }
        return phraseMatches;
    }

    private void addNewPhraseMatchAndClearBuffer(List<List<Match>> phraseMatches, List<VariantGraph.Vertex> basePhrase, List<Token> witnessPhrase) {
        if (!basePhrase.isEmpty()) {
            phraseMatches.add(Match.createPhraseMatch(basePhrase, witnessPhrase));
            basePhrase.clear();
            witnessPhrase.clear();
        }
    }
}
