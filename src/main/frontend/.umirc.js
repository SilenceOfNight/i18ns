const path = require('path');

// ref: https://umijs.org/config/
export default {
  plugins: [
    // ref: https://umijs.org/plugin/umi-plugin-react.html
    [
      'umi-plugin-react',
      {
        antd: true,
        dva: true,
        dynamicImport: true,
        title: 'frontend',
        dll: true,
        routes: {
          exclude: [/model(s\/)?/],
        },
        hardSource: true,
        title: 'I18n MGT',
      },
    ],
  ],
  chainWebpack(config) {
    config.resolve.alias.set('@', path.resolve(__dirname, 'src'));
    config.devServer.proxy({
      '/apis/v1': {
        target: 'http://127.0.0.1:8080',
      },
    });
  },
};
