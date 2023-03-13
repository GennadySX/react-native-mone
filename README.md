# react-native-mone

React Native image filters and effects library.

## Warning ⚠️
This package is still in development and not ready for production use.

## Installation

```sh
npm install react-native-mone
```

## Usage

```js
import { MoneView, MoneFilters } from "react-native-mone";

// ...

const source = {
  uri: "https://i.imgur.com/0Z0Z0Z0.jpg",
  width: 100,
  height: 100,
};

// ...



<MoneView
  source={source}
  width={100}
  height={100}
  filter={MoneFilters.sepia}

/>
```

## Contributing

See the [contributing guide](CONTRIBUTING.md) to learn how to contribute to the repository and the development workflow.

## License

MIT

---

Made with ❤️ by [GennadySX](https://github.com/GennadySX)
