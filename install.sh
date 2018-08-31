

# Prerequsities
yum install gcc-c++ patch readline readline-devel zlib zlib-devel \
    libyaml-devel libffi-devel openssl-devel make \
    bzip2 autoconf automake libtool bison iconv-devel sqlite-devel \

# Install RVM
curl -sSL https://rvm.io/mpapis.asc | gpg --import -
curl -L get.rvm.io | bash -s stable

source /etc/profile.d/rvm.sh
rvm reload

# Verify Dependencies
rvm requirements run

# Install Ruby 2.4
rvm install 2.4


# Install Jekyll and Bundler
#sudo dnf install ruby ruby-devel @development-tools
gem install jekyll bundler


# start Jekyll
bundle install
bundle exec jekyll serve
