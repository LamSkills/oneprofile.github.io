## Starting to work on oneprofile project

As I am new in the Linux world, it was a bit difficult to make the playbook work on Fedora 28

### Fork and Import the project

I was lost when I had to clone all repositories of the project, and name of a module has changed.

Do not forget to fork all projects listed in /home/user/oneprofile/init.sh file before cloning any project.

"Clone this" section of readme.md should be replaced by :

```sh
cd ~/oneprofile/ubuntu
```

Furthermore, it can be very useful to make a script that fork all the projects on a github account and replace the URL of original repo by the forker github account URL

### Editing with vi

It was very interesting to learn vi basics while working on the playbook.

Tip : I enabled branch display on my terminal to facilitate the use of git with Fedora terminal

```sh
vi ~/.bashrc
```

Then put these lines at the end of the file (to insert, press 'i' on your keyboard, paste your clipboard then "Esc" to exit insert mode) :

```sh
export GIT_PS1_SHOWDIRTYSTATE=true
export GIT_PS1_SHOWUNTRACKEDFILES=true
source /usr/share/git-core/contrib/completion/git-prompt.sh
export PS1='[\u@\h \W$(declare -F __git_ps1 &>/dev/null && __git_ps1 " (%s)")]\$
```

### Learning Ansible

It is destabilizing to start learning Ansible from scratch, even more when you do not have enough knowledge in devops. But it was very cool to understand how tasks are executed, how to call scripts, how to debug, understand the function of many Ansible keywords, etc.

### Debugging the Ansible playbook

#### rpmfusion

There is a bug when the rpmfusion's role was executed : "Invalid version flag : or". It is reported on RedHat website : https://bugzilla.redhat.com/show_bug.cgi?id=1489315

To solve this issue, I used dnf repo to install rpmfusion.

Moreover, I changed Fedora version in ansible/config.yml to 28 instead of 26.

#### jdk

I changed jdk version from 161 to 181 as the 1.8.161 version of JDK is now not available.

#### gem

If gem was not present, gem was not installed first, so I created a task which use dnf to install it.
The drush package is not available among all gem packages, so I supposed that this package is now named drush-deploy, so I decided to install it.

#### atom, slack & sqlectron

I find a bug that I can't solve. The grep command is not working properly with pipelines when used in "command" Ansible property. I found a temporary solution to make the atom package downloaded and installed but I am not very satisfied with my solution.

The when's property condition do not accept accents in value for numeric comparison, which is logic, so I make the version variable empty when the package is not found on the machine.
