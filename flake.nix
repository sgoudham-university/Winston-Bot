{
  inputs = {
    nixpkgs.url = "github:nixos/nixpkgs";
    flake-utils.url = "github:numtide/flake-utils";
    language-servers.url = git+https://git.sr.ht/~bwolf/language-servers.nix;
    language-servers.inputs.nixpkgs.follows = "nixpkgs";
  };

  outputs = { nixpkgs, language-servers, flake-utils, ... }:
    flake-utils.lib.eachDefaultSystem (system:
      let
        pkgs = import nixpkgs { inherit system; };
      in
      {
        devShells.default = let 
          styleGuide = builtins.fetchurl {
            url = "https://raw.githubusercontent.com/sgoudham/dotfiles/644ecec11e3ac07af03471608a2627e8a222d547/private_dot_local/private_share/eclipse/eclipse-java-google-style.xml";
            sha256 = "0jnsygxjj770b0rjmxlh8677xpdwmqaqhwg8a3bzsi7y7ckyv1jr";
          };
        in pkgs.mkShell 
        {
          name = "java-shell";
          buildInputs = [
            pkgs.jdk17_headless
            pkgs.maven
            language-servers.packages.${system}.jdt-language-server
          ];
          shellHook = ''
            export JAVA_HOME="${pkgs.jdk17_headless.home}"
            export MAVEN_HOME="${pkgs.maven}/maven"
            export STYLE_GUIDE="${styleGuide}"
          '';
        };
      }
    );
}
