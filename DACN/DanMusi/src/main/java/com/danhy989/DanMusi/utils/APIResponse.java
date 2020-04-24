package com.danhy989.DanMusi.utils;


import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class APIResponse implements Serializable {

    /** The code status. */
    private int codeStatus;

    /** The message status. */
    private String messageStatus;

    /** The description. */
    private String description;

    /** The took. */
    private long took;

    /** The data. */
    private Object data;

}
