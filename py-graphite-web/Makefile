# $NetBSD$

DISTNAME=	graphite-web-0.9.12
PKGNAME=	${PYPKGPREFIX}-${DISTNAME}
CATEGORIES=	www
MASTER_SITES=	-https://github.com/graphite-project/graphite-web/archive/${PKGVERSION_NOREV}${EXTRACT_SUFX}

MAINTAINER=	pkgsrc-users@NetBSD.org
HOMEPAGE=	https://launchpad.net/graphite
COMMENT=	Enterprise scalable realtime graphing platform
LICENSE=	apache-2.0

DEPENDS+=	${PYPKGPREFIX}-cairo>=1.8.10:../../graphics/py-cairo
DEPENDS+=	${PYPKGPREFIX}-carbon>=${PKGREVISION_NOREV}:../../joyent/py-carbon
DEPENDS+=	${PYPKGPREFIX}-django>=1.3.1:../../www/py-django
DEPENDS+=	${PYPKGPREFIX}-django-tagging>=0.3.1:../../www/py-django-tagging

USE_LANGUAGES=	# none

.include "../../mk/bsd.prefs.mk"

BUILD_DEFS+=		VARBASE GRAPHITE_DIR

GRAPHITE_DIR?=		${VARBASE}/graphite
GRAPHITE_EGDIR?=	share/examples/graphite

REPLACE_SH+=		bin/build-index.sh

SUBST_CLASSES+=		dirs
SUBST_STAGE.dirs=	pre-configure
SUBST_MESSAGE.dirs=	Patching default dirs
SUBST_FILES.dirs=	bin/build-index.sh
SUBST_FILES.dirs+=	conf/graphite.wsgi.example
SUBST_FILES.dirs+=	examples/example-graphite-vhost.conf
SUBST_FILES.dirs+=	setup.py
SUBST_FILES.dirs+=	webapp/graphite/local_settings.py.example
SUBST_SED.dirs=		-e 's,/opt/graphite/conf,${PKG_SYSCONFDIR},g'
SUBST_SED.dirs+=	-e 's,/opt/graphite/storage,${GRAPHITE_DIR},g'
SUBST_SED.dirs+=	-e 's,/opt/graphite,${PREFIX}/graphite,g'
SUBST_VARS.dirs=	GRAPHITE_DIR GRAPHITE_EGDIR PREFIX

PKG_SYSCONFSUBDIR=	graphite

GRAPHITE_CONF_FILES=	dashboard.conf graphite.wsgi graphTemplates.conf

.for file in ${GRAPHITE_CONF_FILES}
CONF_FILES+=		${GRAPHITE_EGDIR}/${file}.example	\
			${PKG_SYSCONFDIR}/${file}
.endfor

PYSETUPINSTALLARGS=	--prefix=${PREFIX}			\
			--install-lib=${PREFIX}/${PYSITELIB}

INSTALLATION_DIRS+=	share/examples/graphite

EGG_NAME=	${DISTNAME:S/graphite-/graphite_/}

pre-build:
	${MKDIR} ${WRKSRC}/graphite/webapp
	${CP} -LR ${WRKSRC}/webapp/content ${WRKSRC}/graphite/webapp
	${RM} -f ${WRKSRC}/bin/build-index.sh.orig

.include "../../lang/python/distutils.mk"
.include "../../mk/bsd.pkg.mk"
