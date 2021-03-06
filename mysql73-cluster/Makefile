# $NetBSD$

DISTNAME=	mysql-cluster-gpl-7.3.6
CATEGORIES=	databases
MASTER_SITES=	${MASTER_SITE_MYSQL:=MySQL-Cluster-7.3/}

PKGNAME=	${DISTNAME:S/-gpl//}
COMMENT=	MySQL Cluster Server 7.3

MAINTAINER=	pkgsrc@joyent.com
HOMEPAGE=	http://www.mysql.com/products/cluster/
LICENSE=	gnu-gpl-v2

CONFLICTS=	{mysql,percona}*-{client,server}-[0-9]*

USE_CMAKE=		yes
USE_LANGUAGES=		c c++
USE_TOOLS+=		bash bison gmake perl:run python:run

INSTALL_TEMPLATES+=	${.CURDIR}/../../joyent/percona/INSTALL

#INFO_FILES=		yes
#UNWRAP_FILES+=		scripts/mysql_config

.include "../../mk/bsd.prefs.mk"

.include "options.mk"

# MYSQL_USER		username of the database administrator
# MYSQL_GROUP		group of the database administrator
# MYSQL_DATADIR		home directory (location of the databases)
# MYSQL_CHARSET		default character set
# MYSQL_EXTRA_CHARSET	additional character set to be compiled in
MYSQL_USER?=		mysql
MYSQL_GROUP?=		mysql
MYSQL_DATADIR?=		${VARBASE}/mysql
MYSQL_CHARSET?=		latin1
MYSQL_COLLATION?=	latin1_swedish_ci
MYSQL_EXTRA_CHARSET?=	all

PKG_USERS_VARS+=	MYSQL_USER
PKG_GROUPS_VARS+=	MYSQL_GROUP

CONFIG_SHELL=		${TOOLS_PATH.bash}

.if ${OPSYS} == "IRIX"
CFLAGS+=		-DIRIX5 -DNEEDS_BSTRING_H
.endif

BUILD_DEFS+=		VARBASE

# paths
CMAKE_ARGS+=		-DINSTALL_BINDIR="bin${BINARCHSUFFIX}"
CMAKE_ARGS+=		-DINSTALL_DOCDIR="share/doc/mysql"
CMAKE_ARGS+=		-DINSTALL_DOCREADMEDIR="share/doc/mysql"
CMAKE_ARGS+=		-DINSTALL_INCLUDEDIR="include/mysql"
CMAKE_ARGS+=		-DINSTALL_INFODIR="info"
CMAKE_ARGS+=		-DINSTALL_LIBDIR="lib${LIBARCHSUFFIX}"
CMAKE_ARGS+=		-DINSTALL_MANDIR="${PKGMANDIR}"
CMAKE_ARGS+=		-DINSTALL_MYSQLSHAREDIR="share/mysql"
CMAKE_ARGS+=		-DINSTALL_MYSQLTESTDIR="share/mysql/test"
CMAKE_ARGS+=		-DINSTALL_PLUGINDIR="lib${LIBARCHSUFFIX}/mysql/plugin"
CMAKE_ARGS+=		-DINSTALL_SBINDIR="sbin${BINARCHSUFFIX}"
CMAKE_ARGS+=		-DINSTALL_SCRIPTDIR="bin${BINARCHSUFFIX}"
CMAKE_ARGS+=		-DINSTALL_SQLBENCHDIR="share/mysql/sql-bench"
CMAKE_ARGS+=		-DINSTALL_SUPPORTFILESDIR="share/mysql"
CMAKE_ARGS+=		-DMYSQL_DATADIR=${MYSQL_DATADIR}

# always use our own readline, not MySQL bundled one
CMAKE_ARGS+=		-DWITH_LIBEDIT=OFF
CMAKE_ARGS+=		-DWITH_READLINE=OFF
CMAKE_ARGS+=		-DDEFAULT_CHARSET=${MYSQL_CHARSET:Q}
CMAKE_ARGS+=		-DDEFAULT_COLLATION=${MYSQL_COLLATION:Q}
CMAKE_ARGS+=		-DWITH_EXTRA_CHARSETS=${MYSQL_EXTRA_CHARSET:Q}
BROKEN_READLINE_DETECTION=	yes # At least on RHEL 5.6

CMAKE_ARGS+=		-DWITH_LIBWRAP=ON
CMAKE_ARGS+=		-DWITHOUT_AUTH_PAM=1 -DWITHOUT_AUTH_PAM_COMPAT=1
CMAKE_ARGS+=		-DHAVE_EMBEDDED_PRIVILEGE_CONTROL=1

CMAKE_ARGS+=		-DENABLE_DTRACE=ON

# Engines
CMAKE_ARGS+=		-DWITH_ARCHIVE_STORAGE_ENGINE=ON
CMAKE_ARGS+=		-DWITH_BLACKHOLE_STORAGE_ENGINE=ON
CMAKE_ARGS+=		-DWITH_CSV_STORAGE_ENGINE=ON
CMAKE_ARGS+=		-DWITH_FEDERATED_STORAGE_ENGINE=ON
CMAKE_ARGS+=		-DWITH_INNOBASE_STORAGE_ENGINE=ON
CMAKE_ARGS+=		-DWITH_PARTITION_STORAGE_ENGINE=ON
CMAKE_ARGS+=		-DWITH_PERFSCHEMA_STORAGE_ENGINE=ON

CMAKE_ARGS+=		-DWITH_NDB_JAVA=OFF

# We only need readline to placate the configure script if we don't build
# the embedded server as it is the only binary linked against "readline".
#BUILDLINK_DEPMETHOD.readline=   build

# Ensure dtrace builds the correct ABI
.if !empty(ABI:M32)
CMAKE_ARGS+=		-DDTRACE_FLAGS="-32"
.elif !empty(ABI:M64)
CMAKE_ARGS+=		-DDTRACE_FLAGS="-64"
.endif

# MySQL 5 depends on builtin functions enabled with i486 and later with GCC.
.if ${OPSYS} == "NetBSD" && !empty(MACHINE_ARCH:Mi386)
GNU_ARCH.i386=		i486
CFLAGS+=		-march=i486
.endif

CPPFLAGS+=		-Dunix
CFLAGS+=		-DUSE_OLD_FUNCTIONS # 3.23.x compat

.if ${OPSYS} != "Interix"
CFLAGS+=		-fPIC -DPIC
CXXFLAGS+=		-fPIC -DPIC
.endif

# force HAVE_CURSES_H on Solaris since the configure script is broken
# and does not properly detect this, breaking the build later on.
.if ${OPSYS} == "SunOS"
CFLAGS+=		-DHAVE_CURSES_H
CXXFLAGS+=		-DHAVE_CURSES_H
.endif

.include "../../mk/pthread.buildlink3.mk"

.if defined(PTHREAD_TYPE) && (${PTHREAD_TYPE} == "pth")
CFLAGS+=		-DSIGNALS_DONT_BREAK_READ
CXXFLAGS+=		-DSIGNALS_DONT_BREAK_READ
.endif

LDFLAGS+=		${COMPILER_RPATH_FLAG}${PREFIX}/lib/mysql

PTHREAD_OPTS+=		require

PKG_GROUPS=		${MYSQL_GROUP}
PKG_USERS=		${MYSQL_USER}:${MYSQL_GROUP}

PKG_GECOS.${MYSQL_USER}=	MySQL database administrator
PKG_HOME.${MYSQL_USER}=		${MYSQL_DATADIR}
PKG_SHELL.${MYSQL_USER}=	${SH}

RCD_SCRIPTS=		mysqld
SMF_NAME=		mysql-cluster
SMF_METHODS=		mysqld ndbd
SMF_INSTANCES=		mysqld ndbd ndb_mgmd

FILES_SUBST+=		HOSTNAME_CMD=${HOSTNAME_CMD:Q}
FILES_SUBST+=		MYSQL_DATADIR=${MYSQL_DATADIR}
FILES_SUBST+=		MYSQL_USER=${MYSQL_USER} MYSQL_GROUP=${MYSQL_GROUP}
MESSAGE_SUBST+=		MYSQL_DATADIR=${MYSQL_DATADIR}
MESSAGE_SUBST+=		MYSQL_USER=${MYSQL_USER} MYSQL_GROUP=${MYSQL_GROUP}
BUILD_DEFS+=		MYSQL_DATADIR

CNFSUBST=		-e 's,@MYSQL_DATADIR@,${MYSQL_DATADIR},g'
CNFSUBST+=		-e 's,@PREFIX@,${PREFIX},g'
CNFSUBST+=		-e 's,@VARBASE@,${VARBASE},g'

REPLACE_PERL+=		mysql-test/lib/v1/mysql-test-run.pl
REPLACE_PERL+=		mysql-test/mysql-stress-test.pl
REPLACE_PERL+=		mysql-test/mysql-test-run.pl
REPLACE_PERL+=		mysql-test/suite/engines/rr_trx/run_stress_tx_rr.pl
REPLACE_PERL+=		mysql-test/suite/funcs_1/lib/DataGen_local.pl
REPLACE_PERL+=		mysql-test/suite/funcs_1/lib/DataGen_modify.pl
REPLACE_PERL+=		mysql-test/suite/funcs_2/lib/gen_charset_utf8.pl
REPLACE_PERL+=		scripts/mysql_convert_table_format.sh
REPLACE_PERL+=		scripts/mysql_find_rows.sh
REPLACE_PERL+=		scripts/mysql_fix_extensions.sh
REPLACE_PERL+=		scripts/mysql_setpermission.sh
REPLACE_PERL+=		scripts/mysql_zap.sh
REPLACE_PERL+=		scripts/mysqlaccess.sh
REPLACE_PERL+=		scripts/mysqld_multi.sh
REPLACE_PERL+=		scripts/mysqldumpslow.sh
REPLACE_PERL+=		scripts/mysqlhotcopy.sh
REPLACE_PERL+=		storage/ndb/memcache/memclient.pl

REPLACE_PYTHON+=	mysql-test/suite/opt_trace/validate_json.py
REPLACE_PYTHON+=	storage/ndb/mcc/ndb_setup.py
REPLACE_PYTHON+=	storage/ndb/mcc/tst/tstcli.py

SUBST_CLASSES+=		scr
SUBST_STAGE.scr=	pre-configure
SUBST_FILES.scr=	scripts/mysql_install_db.sh scripts/mysqld_safe.sh \
			scripts/mysql_secure_installation.sh \
			support-files/mysql.server.sh
SUBST_SED.scr=		-e "s,chown,${CHOWN},g"
SUBST_SED.scr+=		-e "s,/bin/sh,${RCD_SCRIPTS_SHELL},g"
SUBST_MESSAGE.scr=	Fixing scripts.

SUBST_CLASSES+=		vars
SUBST_STAGE.vars=	pre-configure
SUBST_FILES.vars=	scripts/mysql_install_db.pl.in
SUBST_VARS.vars=	PKG_SYSCONFDIR

CONF_FILES+=		share/mysql/my-cluster.cnf ${PKG_SYSCONFDIR}/my.cnf
CONF_FILES+=		share/mysql/mysql-cluster.ini ${PKG_SYSCONFDIR}/mysql-cluster.ini

pre-build:
	${RM} -f ${WRKSRC}/storage/ndb/mcc/frontend/dojo/dojox/mobile/build/build.sh.orig

post-install:
	${SED} ${CNFSUBST} ${FILESDIR}/my-cluster.cnf \
		> ${DESTDIR}${PREFIX}/share/mysql/my-cluster.cnf
	${SED} ${CNFSUBST} ${FILESDIR}/mysql-cluster.ini \
		> ${DESTDIR}${PREFIX}/share/mysql/mysql-cluster.ini

.include "../../devel/readline/buildlink3.mk"
.include "../../devel/zlib/buildlink3.mk"
.include "../../lang/python/application.mk"
.include "../../security/openssl/buildlink3.mk"
.include "../../security/tcp_wrappers/buildlink3.mk"
#.include "../../mk/java-vm.mk"
.include "../../mk/bsd.pkg.mk"
