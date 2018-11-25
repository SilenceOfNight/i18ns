import { PureComponent } from 'react';

class Toggle extends PureComponent {
  constructor(props) {
    super(props);

    const { initState = false } = props;

    this.state = {
      state: initState,
    };
  }

  handleToggleState = () => {
    this.setState(preState => {
      const { state } = preState;

      return {
        state: !state,
      };
    });
  };

  render() {
    const { children } = this.props;
    const { state } = this.state;
    return children(state, this.handleToggleState);
  }
}

export default Toggle;
