import ko from 'knockout';
import { login } from '../services/apiService.js';

function LoginViewModel() {
  this.username = ko.observable('');
  this.password = ko.observable('');
  this.messages = ko.observableArray([]);

  this.doLogin = async () => {
    try {
      const user = await login({ username: this.username(), password: this.password() });
      this.messages([{ severity: 'confirmation', summary: 'Logged in', detail: `Welcome ${user.username}` }]);
    } catch (e) {
      this.messages([{ severity: 'error', summary: 'Login failed', detail: e.message }]);
    }
  };
}

export default LoginViewModel;
