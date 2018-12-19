import React, { forwardRef } from 'react';
import styled from 'styled-components';

const Container = styled.label.attrs({
  className: 'file-uploader-wrapper',
})`
  display: inline-block;
`;

const Uploader = styled.input.attrs({ type: 'file' })`
  clip: rect(0, 0, 0, 0);
  height: 0;
  position: absolute;
  width: 0;
`;

const FileUploader = forwardRef(({ children, wapper, ...args }, ref) => {
  return (
    <Container {...wapper}>
      <Uploader {...args} ref={ref} />
      {children}
    </Container>
  );
});

export default FileUploader;
