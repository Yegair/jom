const DEFAULT_ORGANIZATION_NAME = 'Yegair';
const DEFAULT_PROJECT_NAME = 'jom';

const githubRepo = process.env['GITHUB_REPOSITORY'] || `${DEFAULT_ORGANIZATION_NAME}/${DEFAULT_PROJECT_NAME}`;

const [
  organizationName = DEFAULT_ORGANIZATION_NAME,
  projectName = DEFAULT_PROJECT_NAME
] = githubRepo.split('/');

module.exports = {
  title: 'jom',
  tagline: 'Kotlin/JVM parser combinator library',
  url: `https://yegair.io/${projectName}`,
  baseUrl: `/${projectName}/`,
  onBrokenLinks: 'throw',
  onBrokenMarkdownLinks: 'warn',
  favicon: 'img/favicon.ico',
  organizationName,
  projectName,
  themeConfig: {
    navbar: {
      title: projectName,
      logo: {
        alt: `${projectName} Logo`,
        src: 'img/logo.svg',
      },
      items: [
        {
          type: 'doc',
          docId: 'overview',
          label: 'Docs',
          position: 'left',
        },
        {
          // we expect the Dokka generated HTML to be present in static/api
          // this is done within the GitHub workflow which builds and deploys the pages
          href: '/api/jom/index.html',
          label: 'API',
          position: 'left'
        },
        {
          type: 'doc',
          docId: 'examples/json',
          label: 'Examples',
          position: 'left',
        },
        {
          href: `https://github.com/${githubRepo}`,
          label: 'GitHub',
          position: 'right',
        },
      ],
    },
    footer: {
      style: 'dark',
      links: [
        {
          title: 'Docs',
          items: [
            {
              label: 'Getting Started',
              to: 'docs/getting-started',
            },
            {
              label: 'API',
              to: 'docs/api/Input',
            },
          ],
        },
        {
          title: 'Community',
          items: [
            {
              label: 'Stack Overflow',
              href: `https://stackoverflow.com/questions/tagged/${projectName}`,
            },
          ],
        },
        {
          title: 'More',
          items: [
            {
              label: 'GitHub',
              href: `https://github.com/${githubRepo}`,
            },
          ],
        },
      ],
      copyright: `Copyright © ${new Date().getFullYear()} Hauke Jäger. Built with Docusaurus.`,
    },
  },
  presets: [
    [
      '@docusaurus/preset-classic',
      {
        docs: {
          sidebarPath: require.resolve('./sidebars.js'),
          // Please change this to your repo.
          editUrl: `https://github.com/${githubRepo}/edit/main/pages/`,
          routeBasePath: 'docs'
        },
        theme: {
          customCss: require.resolve('./src/css/custom.css'),
        },
      },
    ],
  ],
};
