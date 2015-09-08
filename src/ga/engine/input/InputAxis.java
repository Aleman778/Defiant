/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ga.engine.input;

import javafx.scene.input.KeyCode;

public final class InputAxis {
    
    private final KeyCode keyNeg, keyPos;
    
    public float value;
    
    public InputAxis(KeyCode keyNeg, KeyCode keyPos) {
        this.keyNeg = keyNeg;
        this.keyPos = keyPos;
        this.value = 0.0f;
    }
 
    public KeyCode getKeyPos() {
        return keyPos;
    }
    
    public KeyCode getKeyNeg() {
        return keyNeg;
    }
    
}
