# Copyright 2018 (C) O.S. Systems Software LTDA.
SUMMARY = "Samples for OpenGL ES"
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://LICENSE;md5=aeb969185a143c3c25130bc2c3ef9a50"
DEPENDS = "imx-gpu-viv zlib libpng procps"

SRC_URI = "\
       git://source.codeaurora.org/external/imx/apitrace-imx.git;protocol=https;branch=imx_7.1 \
       file://0001-switch-from-python2-to-python3.patch \
       file://0002-specs-Tie-Python-2-3-conversion-loose-ends.patch \
       file://0003-scripts-Tie-Python-2-3-conversion-loose-ends.patch \
       file://0004-scripts-Tie-a-few-more-Python-2-to-3-conversion-loos.patch \
"
SRCREV = "09579e67262af9c993dd082055a924c2c61cf34d"

S = "${WORKDIR}/git"

inherit cmake lib_package pkgconfig perlnative python3native

PACKAGECONFIG ??= ""
PACKAGECONFIG_append = \
    "${@bb.utils.contains('DISTRO_FEATURES', 'wayland', '', \
        bb.utils.contains('DISTRO_FEATURES',     'x11', ' x11', \
                                                        '', d), d)}"
PACKAGECONFIG_append_imxgpu2d = " vivante"
# For 8M, which has 3D but no 2D, eglretrace is not available
# on Wayland except through X11 and waffle.
PACKAGECONFIG_IMXGPU3D = \
    "${@bb.utils.contains('DISTRO_FEATURES', 'wayland x11', ' waffle x11 x11-egl', '', d)}"
PACKAGECONFIG_IMXGPU3D_imxgpu2d = ""
PACKAGECONFIG_append_imxgpu3d = "${PACKAGECONFIG_IMXGPU3D}"

PACKAGECONFIG[multiarch] = "-DENABLE_MULTIARCH=ON,-DENABLE_MULTIARCH=OFF"
PACKAGECONFIG[waffle] = "-DENABLE_WAFFLE=ON,-DENABLE_WAFFLE=OFF,waffle"
PACKAGECONFIG[x11] = "-DENABLE_X11=ON,-DENABLE_X11=OFF"
PACKAGECONFIG[x11-egl] = "-Dwaffle_has_x11_egl=ON,-Dwaffle_has_x11_egl=OFF"
PACKAGECONFIG[vivante] = "-DENABLE_VIVANTE=ON,-DENABLE_VIVANTE=OFF,virtual/libg2d"

FILES_${PN} = "${bindir} ${libdir}"
FILES_${PN}-dbg += "${libdir}/*/*/.debug"

PACKAGE_ARCH = "${MACHINE_SOCARCH}"
COMPATIBLE_MACHINE = "(imxgpu)"
