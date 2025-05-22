package com.dungeoncode.ca.automa;

import com.dungeoncode.ca.automa.rules.RuleCandleRain;
import com.dungeoncode.ca.core.AbstractConfiguration;
import com.dungeoncode.ca.core.Automaton;
import com.dungeoncode.ca.core.impl.BooleanCell;
import com.dungeoncode.ca.core.impl.BooleanState;
import com.dungeoncode.ca.core.impl.init.InitRandomBoolean;

import java.util.List;

/**
 * Configures an {@link Automaton} to run the CANDLE-RAIN cellular automaton, simulating candles being extinguished by
 * random raindrops with an exponential decay pattern. The grid is initialized with a random distribution of active
 * (lit) and inactive (blown out) candles in the state (50% probability) and a random pattern in the echo (50%
 * probability) using {@link InitRandomBoolean}. This configuration is inspired by the CANDLE-RAIN rule
 * described in Chapter 8, Sections 8.1-8.2 of <i>Cellular Automata Machines: A New Environment for Modeling</i>.
 *
 * @see RuleCandleRain
 * @see InitRandomBoolean
 */
public class ConfCandleRain extends AbstractConfiguration<BooleanCell, BooleanState> {

    /**
     * Constructs a new CANDLE-RAIN configuration with metadata for name, description, and citation. The configuration
     * is named "Exponential Candle Decay," described as a cellular automaton simulating exponential decay of candles,
     * and cites the book by Toffoli and Margolus.
     */
    public ConfCandleRain() {
        super(18, new InitRandomBoolean(), List.of(new RuleCandleRain()));
    }

}