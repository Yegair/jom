import * as React from 'react';
import useDocusaurusContext from '@docusaurus/useDocusaurusContext';

export const ProjectVersion = () => {
    const {siteConfig} = useDocusaurusContext();
    return (
        <code>{siteConfig.customFields.projectVersion}</code>
    )
};
