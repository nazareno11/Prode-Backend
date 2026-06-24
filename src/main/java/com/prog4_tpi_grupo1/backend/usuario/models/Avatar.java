package com.prog4_tpi_grupo1.backend.usuario.models;

public enum Avatar {

    DEFAULT ("/avatars/default.png"),
    AVATAR_1("/avatars/avatar_1.png"),
    AVATAR_2("/avatars/avatar_2.png"),
    AVATAR_3("/avatars/avatar_3.png"),
    AVATAR_4("/avatars/avatar_4.png"),
    AVATAR_5("/avatars/avatar_5.png");


    //evitar maping manual
    private final String url;

    Avatar(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }
}
