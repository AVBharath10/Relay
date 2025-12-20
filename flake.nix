{
  description = "Relay: Distributed Rate Limiter Development Environment";

  inputs = {
    nixpkgs.url = "github:NixOS/nixpkgs/nixos-unstable";
    flake-utils.url = "github:numtide/flake-utils";
  };

  outputs = { self, nixpkgs, flake-utils }:
    flake-utils.lib.eachDefaultSystem (system:
      let
        pkgs = import nixpkgs { inherit system; };
        javaVersion = pkgs.jdk21;
      in
      {
        devShells.default = pkgs.mkShell {
          buildInputs = with pkgs; [
            javaVersion
            maven
            redis
            jdt-language-server
          ];

          shellHook = ''
            export JAVA_HOME=${javaVersion}
            echo " Relay Development Environment"
            echo " Java: $(java -version 2>&1 | head -n 1)"
            echo " Maven: $(mvn -version | head -n 1)"
            echo " Redis: $(redis-server --version | head -n 1)"
          '';
        };

        packages.default = pkgs.maven.buildMavenPackage {
          pname = "relay";
          version = "0.1.0";
          src = ./.;
          mvnHash = "sha256-AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA="; # Update this after first build attempt
          jdk = javaVersion;
        };
      }
    );
}
