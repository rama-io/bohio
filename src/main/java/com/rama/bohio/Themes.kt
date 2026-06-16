package com.rama.bohio

object Themes {
    data class Palette(
        val foreground: Int,
        val bg_1: Int,
        val bg_2: Int,
        val bg_3: Int,
        val accent_1: Int,
        val accent_2: Int,
        val accent_3: Int,
        val disabled: Int,
        val input: Int,
        val button_1: Int,
        val button_2: Int,
        val danger: Int,
        val collapsible_header: Int,
        val icon: Int,
        val h1: Int,
    )

    // Teyin (default)
    val TEYIN = Palette(
        foreground = 0xFFEAE6DF.toInt(),
        bg_1 = 0xFF0E181A.toInt(),
        bg_2 = 0xFF162A2E.toInt(),
        bg_3 = 0xFF1E383D.toInt(),
        accent_1 = 0xFF4DA8AC.toInt(),
        accent_2 = 0xFFD4AF37.toInt(),
        accent_3 = 0xFFC77D4D.toInt(),
        disabled = 0xFF5D7A7C.toInt(),
        input = 0xFF0C1416.toInt(),
        button_1 = 0xFF3A9DA0.toInt(),
        button_2 = 0xFFC49B2E.toInt(),
        danger = 0xFFB83A2D.toInt(),
        collapsible_header = 0xFF7A9495.toInt(),
        icon = 0xFFC8C2B8.toInt(),
        h1 = 0xFFEAE6DF.toInt(),
    )

    // Mako
    val MAKO = Palette(
        foreground = 0xFFCCCCCC.toInt(),
        bg_1 = 0xFF141417.toInt(),
        bg_2 = 0xFF1F1F29.toInt(),
        bg_3 = 0xFF24313b.toInt(),
        accent_1 = 0xFFABD68E.toInt(),
        accent_2 = 0xFFCDC58B.toInt(),
        accent_3 = 0xFFDCD07C.toInt(),
        disabled = 0xFF888888.toInt(),
        input = 0xFF16161F.toInt(),
        button_1 = 0xFF459984.toInt(),
        button_2 = 0xFF6194AF.toInt(),
        danger = 0xFFDC6364.toInt(),
        collapsible_header = 0xFF878787.toInt(),
        icon = 0xFFCBCBCB.toInt(),
        h1 = 0xFFCACACA.toInt(),
    )

    // Rama
    val RAMA = Palette(
        foreground = 0xFFcbdecd.toInt(),
        bg_1 = 0xFF0e190e.toInt(),
        bg_2 = 0xFF1f2920.toInt(),
        bg_3 = 0xFF2d3b24.toInt(),
        accent_1 = 0xFFABD68E.toInt(),
        accent_2 = 0xFFCDC58B.toInt(),
        accent_3 = 0xFFDCD07C.toInt(),
        disabled = 0xFF888888.toInt(),
        input = 0xFF161f16.toInt(),
        button_1 = 0xFF45995a.toInt(),
        button_2 = 0xFFb8e39d.toInt(),
        danger = 0xFFDC6364.toInt(),
        collapsible_header = 0xff8cde285.toInt(),
        icon = 0xFFd4efc3.toInt(),
        h1 = 0xFFABD68E.toInt(),
    )

    // Catppuccin Mocha
    val CATPPUCCIN_MOCHA = Palette(
        foreground = 0xFFCDD6F4.toInt(),
        bg_1 = 0xFF1E1E2E.toInt(),
        bg_2 = 0xFF313244.toInt(),
        bg_3 = 0xFF45475A.toInt(),
        accent_1 = 0xFFA6E3A1.toInt(),
        accent_2 = 0xFFF9E2AF.toInt(),
        accent_3 = 0xFFFFD700.toInt(),
        disabled = 0xFF6C7086.toInt(),
        input = 0xFF181825.toInt(),
        button_1 = 0xFF89B4FA.toInt(),
        button_2 = 0xFF74C7EC.toInt(),
        danger = 0xFFF38BA8.toInt(),
        collapsible_header = 0xFFB4BEFE.toInt(),
        icon = 0xFFCDD6F4.toInt(),
        h1 = 0xFFCBA6F7.toInt(),
    )

    val CATPPUCCIN_LATTE = Palette(
        foreground = 0xFF4C4F69.toInt(),
        bg_1 = 0xFFEFF1F5.toInt(),
        bg_2 = 0xFFCCD0DA.toInt(),
        bg_3 = 0xFFBCC0CC.toInt(),

        accent_1 = 0xFF40A02B.toInt(),
        accent_2 = 0xFFDF8E1D.toInt(),
        accent_3 = 0xFFFE640B.toInt(),

        disabled = 0xFF9CA0B0.toInt(),
        input = 0xFFE6E9EF.toInt(),

        button_1 = 0xFF1E66F5.toInt(),
        button_2 = 0xFF04A5E5.toInt(),

        danger = 0xFFD20F39.toInt(),
        collapsible_header = 0xFF7287FD.toInt(),
        icon = 0xFF4C4F69.toInt(),
        h1 = 0xFF8839EF.toInt()
    )


    // Dracula
    val DRACULA = Palette(
        foreground = 0xFFF8F8F2.toInt(),
        bg_1 = 0xFF282A36.toInt(),
        bg_2 = 0xFF363849.toInt(),
        bg_3 = 0xFF424450.toInt(),
        accent_1 = 0xFF50FA7B.toInt(),
        accent_2 = 0xFFF1FA8C.toInt(),
        accent_3 = 0xFFFFB86C.toInt(),
        disabled = 0xFF6272A4.toInt(),
        input = 0xFF21222C.toInt(),
        button_1 = 0xFFBD93F9.toInt(),
        button_2 = 0xFF8BE9FD.toInt(),
        danger = 0xFFFF79C6.toInt(),
        collapsible_header = 0xFFBD93F9.toInt(),
        icon = 0xFFF8F8F2.toInt(),
        h1 = 0xFFBD93F9.toInt(),
    )

    // Melange Dark
    val MELANGE = Palette(
        foreground = 0xFFECE1D7.toInt(),
        bg_1 = 0xFF292522.toInt(),
        bg_2 = 0xFF352F2A.toInt(),
        bg_3 = 0xFF403A34.toInt(),
        accent_1 = 0xFF78997A.toInt(),
        accent_2 = 0xFFEBC06D.toInt(),
        accent_3 = 0xFFE49B5D.toInt(),
        disabled = 0xFF867462.toInt(),
        input = 0xFF211E1B.toInt(),
        button_1 = 0xFF7F91B2.toInt(),
        button_2 = 0xFF85B695.toInt(),
        danger = 0xFFB65C60.toInt(),
        collapsible_header = 0xFFEBC06D.toInt(),
        icon = 0xFFECE1D7.toInt(),
        h1 = 0xFFEBC06D.toInt(),
    )

    // Tokyo Night
    val TOKYO_NIGHT = Palette(
        foreground = 0xFFC0CAF5.toInt(),
        bg_1 = 0xFF1A1B26.toInt(),
        bg_2 = 0xFF24283B.toInt(),
        bg_3 = 0xFF292E42.toInt(),
        accent_1 = 0xFF9ECE6A.toInt(),
        accent_2 = 0xFFE0AF68.toInt(),
        accent_3 = 0xFFFF9E64.toInt(),
        disabled = 0xFF565F89.toInt(),
        input = 0xFF16161E.toInt(),
        button_1 = 0xFF7AA2F7.toInt(),
        button_2 = 0xFF2AC3DE.toInt(),
        danger = 0xFFF7768E.toInt(),
        collapsible_header = 0xFF7AA2F7.toInt(),
        icon = 0xFFC0CAF5.toInt(),
        h1 = 0xFF7AA2F7.toInt(),
    )
}